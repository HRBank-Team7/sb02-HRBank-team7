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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryCustomImpl implements EmployeeRepositoryCustom {

  @PersistenceContext
  private EntityManager em;

  EmployeeMapper employeeMapper;

  @Override
  public List<Employee> searchEmployees(EmployeeSearchCondition condition) {

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Employee> contentQuery = cb.createQuery(Employee.class);
    Root<Employee> employee = contentQuery.from(Employee.class);
    contentQuery.select(employee);

    List<Predicate> predicates = new ArrayList<>();

//    이름 or 이메일 검색
    if(condition.nameOrEmail() != null){
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

//    정렬 조건 적용
    if("asc".equalsIgnoreCase(condition.sortDirection())){ // sortDirection이 asc 라면
      if(condition.idAfter() != null && condition.cursor() != null){
        if(condition.sortField().equals("hireDate")) {
          LocalDate cursorDate = LocalDate.parse(condition.cursor());
          predicates.add(cb.or(
              cb.greaterThan(employee.get(condition.sortField()), cursorDate),
              cb.and(
                  cb.equal(employee.get(condition.sortField()), cursorDate),
                  cb.greaterThan(employee.get("id"), condition.idAfter())
              )
          ));
        } else {
          predicates.add(cb.or(
              cb.greaterThan(employee.get(condition.sortField()), condition.cursor()),
              cb.and(
                  cb.equal(employee.get(condition.sortField()), condition.cursor()),
                  cb.greaterThan(employee.get("id"), condition.idAfter())
              )
          ));
        }
      }
      contentQuery.orderBy(cb.asc(employee.get(condition.sortField()))); // sortField 기준으로 오름차순
    } else if ("desc".equalsIgnoreCase((condition.sortDirection()))){
      if(condition.idAfter() != null && condition.cursor() != null){
        if(condition.sortField().equals("hireDate")) {
          LocalDate cursorDate = LocalDate.parse(condition.cursor());
          predicates.add(cb.or(
              cb.lessThan(employee.get(condition.sortField()), cursorDate),
              cb.and(
                  cb.equal(employee.get(condition.sortField()), cursorDate),
                  cb.lessThan(employee.get("id"), condition.idAfter())
              )
          ));
        } else {
          predicates.add(cb.or(
              cb.lessThan(employee.get(condition.sortField()), condition.cursor()),
              cb.and(
                  cb.equal(employee.get(condition.sortField()), condition.cursor()),
                  cb.lessThan(employee.get("id"), condition.idAfter())
              )
          ));
        }
      }
      contentQuery.orderBy(cb.desc(employee.get(condition.sortField())));
    }

    if (!predicates.isEmpty()){
      contentQuery.where(predicates.toArray(new Predicate[0]));
    }

    TypedQuery<Employee> query = em.createQuery(contentQuery)
        .setMaxResults(condition.pageable().getPageSize());

    return query.getResultList();
  }
}



//public CursorPageEmployeeResponse<EmployeeResponse> getAllEmployees(cursor, size ) {
//  디코딩
//  이랑 컨디션 객첵 생성만 추가
//  Page<Employee> employee = employeeRepository.findEmployeeList(request); // Page == List
//  Page<EmployeeResponse> employeeResponsePage = employee.map(employeeMapper::toResponse);
//  return employeeMapper.toResponse(employeeResponsePage);
