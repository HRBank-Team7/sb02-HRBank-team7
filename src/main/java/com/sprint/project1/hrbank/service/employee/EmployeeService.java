package com.sprint.project1.hrbank.service.employee;

import com.sprint.project1.hrbank.dto.employee.EmployeeCreateRequest;
import com.sprint.project1.hrbank.dto.employee.EmployeeResponse;

public interface EmployeeService {
  EmployeeResponse createEmployee(EmployeeCreateRequest request);
}
