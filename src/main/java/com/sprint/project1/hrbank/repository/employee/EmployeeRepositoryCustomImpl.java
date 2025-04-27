package com.sprint.project1.hrbank.repository.employee;

import com.sprint.project1.hrbank.dto.employee.EmployeeSearchCondition;
import com.sprint.project1.hrbank.entity.employee.Employee;
import com.sprint.project1.hrbank.mapper.employee.EmployeeMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryCustomImpl implements EmployeeRepositoryCustom {

  @PersistenceContext
  private EntityManager em;

  EmployeeMapper employeeMapper;

  @Override
  public List<Employee> searchEmployees(EmployeeSearchCondition condition) {

//     EntityManager 로 부터 CriteriaBuilder 를 받아옴. <- query 생성하는 녀석
    CriteriaBuilder cb = em.getCriteriaBuilder();

//     contentQuery 의 반환 값을 지정(여기서는 Employee 타입으로 반환 값 지정)
    CriteriaQuery<Employee> contentQuery = cb.createQuery(Employee.class);

    // 쿼리 시작 점 지정. FROM Employee 에 해당. 어떤 테이블에서 조회할 지 선택
    Root<Employee> employee = contentQuery.from(Employee.class);

//     SELECT *
    contentQuery.select(employee);
//     위 2 개를 합쳐서 SELECT e FROM Employee e

//    List<Employee> employees = em.createQuery(contentQuery).getResultList();
//    이렇게 자바 객체로 받아서 사용할 수 있다.
//    자바 객체에서 조건을 검색하는건 굉장히 비효율적이고, DB 에서 조건 검색을 해야한다.

//    조건(Predicate) 리스트 생성. SQL의 WHERE 절에 들어갈 조건을 나타내는 객체.
//    조건을 담은 리스트, 조건을 add 로 늘려갈 수 있다.
//    contentQuery.where(predicates.toArray(new Predicate[0]));
//    return em.createQuery(contentQuery).getResultList();
//    orderBy()
    List<Predicate> predicates = new ArrayList<>();

//    이름 or 이메일 검색
    if(condition.nameOrEmail() != null){
//      LIKE %nameOrEmail%
      String nameOrEmail = "%" + condition.nameOrEmail() + "%";
      predicates.add(cb.or(
          cb.like(employee.get("name"), nameOrEmail),
          cb.like(employee.get("email"), nameOrEmail)
      ));
    }

//    사원 번호 검색
    if(condition.employeeNumber() != null){
      String employeeNumber = "%" + condition.employeeNumber() + "%";
      predicates.add(cb.like(employee.get("employeeNumber"), employeeNumber));
    }

    if(condition.departmentName() != null){
      String departmentName = "%" + condition.departmentName() + "%";
      predicates.add(cb.like(employee.get("department").get("name"), departmentName));
    }

    if(condition.position() != null){
      String position = "%" + condition.position() + "%";
      predicates.add(cb.like(employee.get("position"), position));
    }

//    입사일 조건 검색
    if(condition.hireDateFrom() != null){
      predicates.add(cb.greaterThanOrEqualTo(
          employee.get("hireDate"), condition.hireDateFrom()));
    }
    if(condition.hireDateTo() != null){
      predicates.add(cb.lessThanOrEqualTo(
          employee.get("hireDate"), condition.hireDateTo()));
    }

//    상태 조건 검색
    if(condition.status() != null){
      predicates.add(cb.equal(employee.get("status"), condition.status()));
    }

//    if(condition.idAfter() != null && condition.cursor() != null){
//      predicates.add(cb.greaterThan(employee.get("cursor"), condition.cursor()));
//      predicates.add(cb.greaterThan(employee.get("id"), condition.idAfter())); // 중복인 경우 id로 비교
//    }
    // 오름차순, 내림차순

//    정렬 조건 적용
    if("asc".equalsIgnoreCase(condition.sortDirection())){ // sortDirection이 asc 라면
      if(condition.idAfter() != null && condition.cursor() != null){
        predicates.add(cb.greaterThan(employee.get("cursor"), condition.cursor()));
        predicates.add(cb.greaterThan(employee.get("id"), condition.idAfter())); // 중복인 경우 id로 비교
      }
      contentQuery.orderBy(cb.asc(employee.get(condition.sortField()))); // sortField 기준으로 오름차순
    }else if("desc".equalsIgnoreCase((condition.sortDirection()))){
      if(condition.idAfter() != null && condition.cursor() != null){
        predicates.add(cb.lessThan(employee.get("cursor"), condition.cursor()));
        predicates.add(cb.lessThan(employee.get("id"), condition.idAfter())); // 중복인 경우 id로 비교
      }
      contentQuery.orderBy(cb.desc(employee.get(condition.sortField())));
    }

    if (!predicates.isEmpty()){
      contentQuery.where(predicates.toArray(new Predicate[0]));
    }

//    페이징 적용
//    setMaxResults(request.size()) 한 페이지에 보여질 최대 데이터 개수 설정
//    -> LIMIT 절과 동일
    TypedQuery<Employee> query = em.createQuery(contentQuery)
        .setMaxResults(condition.pageable().getPageSize());
    List<Employee> employees = query.getResultList(); //조건에 맞는 현재 페이지의 Employee 리스트 반환

//    조건에 맞는 전체 개수 조회 (페이징 총 페이지 수 계산)
//    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class); // return type이 Long인 쿼리 생성
//    countQuery.select(cb.count(countQuery.from(Employee.class))); // cb.count 테이블의 개수를 세는 쿼리 생성
//    countQuery.where(predicates.toArray(new Predicate[0])); // 이 전에 정의된 predicates 검색 조건 적용
//    Long total = em.createQuery(countQuery).getSingleResult(); // 조건에 맞는 총 데이터 개수 반환

//    응답 반환
//    Pageable pageable = PageRequest.of(0, request.size() + 1);
//    Page<EmployeeResponse> responsePage = employeeMapper.toResponsePage(employees, pageable, total);
    return employees;
  }
}



//public CursorPageEmployeeResponse<EmployeeResponse> getAllEmployees(cursor, size ) {
//  디코딩
//  이랑 컨디션 객첵 생성만 추가
//  Page<Employee> employee = employeeRepository.findEmployeeList(request); // Page == List
//  Page<EmployeeResponse> employeeResponsePage = employee.map(employeeMapper::toResponse);
//  return employeeMapper.toResponse(employeeResponsePage);

