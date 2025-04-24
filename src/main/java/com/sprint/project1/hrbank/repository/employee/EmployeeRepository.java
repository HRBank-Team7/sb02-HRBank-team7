package com.sprint.project1.hrbank.repository.employee;

import com.sprint.project1.hrbank.entity.employee.Employee;
import com.sprint.project1.hrbank.entity.employee.EmployeeStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Boolean existsByEmail(String email);
  Boolean existsByUpdatedAtAfter(Instant updatedAt);
  Long countByDepartmentId(Long departmentId);

  //기준 날짜 이전 직원 수
  @Query("select count(e) from Employee e where e.createdAt < :before")
  Long countEmployeesBefore(@Param("before") Instant before);

  //부서별 직원 수
  @Query("SELECT e.department.name, COUNT(e) FROM Employee e "
      + "WHERE e.status = :status GROUP BY e.department.name")
  List<Object[]> countByDepartment(@Param("status") EmployeeStatus status);

  //직함별 직원 수
  @Query("SELECT e.position, COUNT(e) FROM Employee e "
      + "WHERE e.status = :status GROUP BY e.position")
  List<Object[]> countByPosition(@Param("status") EmployeeStatus status);

  //status 에 따른 직원 수 조회
  @Query("SELECT COUNT(e) FROM Employee e WHERE e.status = :status")
  Long countByStatus(@Param("status") EmployeeStatus status);

  // 상태별 전체 직원 수 조회
  @Query("select count(e) from Employee e where e.status = :status")
  Long countAllEmployeesWithStatus(@Param("status") EmployeeStatus status);

  // 기간별 직원수 조회
  @Query("select count(e) from Employee e where :fromDate <= e.hireDate and e.hireDate <= :toDate")
  Long countEmployeesByHireDate(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

  // 기간별(상태 포함) 직원수 조회
  @Query("select count(e) from Employee e where e.status = :status and (:fromDate <= e.hireDate and e.hireDate <= :toDate)")
  Long countEmployeesByHireDateWithStatus(@Param("status") EmployeeStatus status, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

}
