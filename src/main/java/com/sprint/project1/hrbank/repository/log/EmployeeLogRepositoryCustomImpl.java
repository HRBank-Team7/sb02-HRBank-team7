package com.sprint.project1.hrbank.repository.log;


import com.sprint.project1.hrbank.dto.log.ChangeLogResponse;
import com.sprint.project1.hrbank.dto.log.EmployeeLogCondition;
import com.sprint.project1.hrbank.entity.log.EmployeeLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class EmployeeLogRepositoryCustomImpl implements EmployeeLogRepositoryCustom{

  @PersistenceContext
  private EntityManager em;

  @Override
  public List<EmployeeLog> searchEmployeeLogs(EmployeeLogCondition condition) {

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<EmployeeLog> contentQuery = cb.createQuery(EmployeeLog.class);
    Root<EmployeeLog> log = contentQuery.from(EmployeeLog.class);
    contentQuery.select(log);

    List<Predicate> predicates = new ArrayList<>();
//  사번 부분 일치 대소문자 구분 X
    if(condition.employeeNumber() != null){
      String employeeNumber = "%" + condition.employeeNumber().toUpperCase() + "%";
      predicates.add(cb.like((log.get("employeeNumber")),employeeNumber));
    }

// 메모(memo) 부분 일치
    if(condition.memo() != null){
      String memo = "%" + condition.memo() + "%";
      predicates.add(cb.like((log.get("memo")),memo));
    }

//    IP주소(String) (부분 일치)
    if(condition.ipAddress() != null){
      String ipAddress = "%" + condition.ipAddress() + "%";
      predicates.add(cb.like((log.get("ipAddress")), ipAddress));
    }

//    type 이력 유형 (CREATED, UPDATED, DELETED) 일치
    if(condition.type() != null){
      String type = "%" + condition.type() + "%";
      predicates.add(cb.like((log.get("type")), type));
    }

    //시작일 종료일 중간
    if(condition.atFrom() != null){ // atFrom 보다 큰 경우 포함
      predicates.add(cb.greaterThanOrEqualTo(log.get("at"), condition.atFrom()));
    }
    if(condition.atTo() != null){ // atTo 보다 작은 경우 포함
      predicates.add(cb.lessThanOrEqualTo(log.get("at"), condition.atTo()));
    }

    //여기서부터 정렬 및 커서, 정렬 기준 (기본값:at, ipAddress <- 이거 앱에서는 id로 구분중)오름차순, 내림 차순
    if("asc".equalsIgnoreCase(condition.sortDirection())){
      if(condition.cursor() != null && condition.idAfter() != null){
        predicates.add(cb.greaterThan(log.get("cursor"), condition.cursor()));
        predicates.add(cb.greaterThan(log.get("id"), condition.idAfter()));
      }
      contentQuery.orderBy(cb.asc(log.get(condition.sortField())));
    } else if("desc".equalsIgnoreCase(condition.sortDirection())){
      if(condition.cursor() != null && condition.idAfter() != null){
        predicates.add(cb.lessThan(log.get("cursor"), condition.cursor()));
        predicates.add(cb.lessThan(log.get("id"), condition.idAfter()));
      }
      contentQuery.orderBy(cb.desc(log.get(condition.sortField())));
    }

    if(!predicates.isEmpty()){
      contentQuery.where(predicates.toArray(new Predicate[0]));
    }

    TypedQuery<EmployeeLog> query = em.createQuery(contentQuery)
        .setMaxResults(condition.pageable().getPageSize());

    return query.getResultList();
  }

  @Override
  public Long getCountByDate(Instant fromDate, Instant toDate) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> contentQuery = cb.createQuery(Long.class);
    Root<EmployeeLog> log = contentQuery.from(EmployeeLog.class);
    contentQuery.select(cb.count(log));

    List<Predicate> predicates = new ArrayList<>();

    //시작일 종료일 중간
    if(fromDate != null){ // atFrom 보다 큰 경우 포함
      predicates.add(cb.greaterThanOrEqualTo(log.get("at"), fromDate));
    }
    if(toDate != null){ // atTo 보다 작은 경우 포함
      predicates.add(cb.lessThanOrEqualTo(log.get("at"), toDate));
    }

    contentQuery.where(predicates.toArray(new Predicate[0]));

    TypedQuery<Long> query = em.createQuery(contentQuery);

    return query.getSingleResult();
  }

}
