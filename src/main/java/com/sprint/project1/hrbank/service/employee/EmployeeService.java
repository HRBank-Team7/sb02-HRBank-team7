package com.sprint.project1.hrbank.service.employee;

import com.sprint.project1.hrbank.dto.employee.EmployeeCreateRequest;
import com.sprint.project1.hrbank.dto.employee.EmployeeDistributionResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeTrendResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeUpdateRequest;
import com.sprint.project1.hrbank.entity.employee.EmployeeStatus;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {
  EmployeeResponse createEmployee(EmployeeCreateRequest request);

  void deleteEmployee(Long id);

  EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request);

  List<EmployeeTrendResponse> getEmployeeTrend(LocalDate from, LocalDate to, String unit);

  List<EmployeeDistributionResponse> getEmployeeDistribution(String groupBy, EmployeeStatus status);

  Long getEmployeeByCount(EmployeeStatus status, LocalDate fromDate, LocalDate toDate);
}
