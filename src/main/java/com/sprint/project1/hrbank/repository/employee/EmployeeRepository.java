package com.sprint.project1.hrbank.repository.employee;

import com.sprint.project1.hrbank.entity.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Boolean existsByEmail(String email);

}
