package com.sprint.project1.hrbank.service.employee;

import com.sprint.project1.hrbank.dto.employee.EmployeeCreateRequest;
import com.sprint.project1.hrbank.dto.employee.EmployeeResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeUpdateRequest;

public interface EmployeeService {
  EmployeeResponse createEmployee(EmployeeCreateRequest request);

  void deleteEmployee(Long id);

  EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request);

}
