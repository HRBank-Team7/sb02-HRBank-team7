package com.sprint.project1.hrbank.repository.employee;

import com.sprint.project1.hrbank.dto.employee.CursorPageEmployeeResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeSearchCondition;
import com.sprint.project1.hrbank.dto.employee.EmployeeSearchRequest;
import com.sprint.project1.hrbank.entity.employee.Employee;
import java.util.List;

public interface EmployeeRepositoryCustom {
  List<Employee> searchEmployees(EmployeeSearchCondition condition);
}
