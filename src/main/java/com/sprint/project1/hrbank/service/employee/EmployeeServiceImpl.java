package com.sprint.project1.hrbank.service.employee;

import com.sprint.project1.hrbank.dto.employee.EmployeeCreateRequest;
import com.sprint.project1.hrbank.dto.employee.EmployeeDistributionResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeTrendResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeUpdateRequest;
import com.sprint.project1.hrbank.entity.department.Department;
import com.sprint.project1.hrbank.entity.employee.Employee;
import com.sprint.project1.hrbank.entity.employee.EmployeeStatus;
import com.sprint.project1.hrbank.mapper.employee.EmployeeMapper;
import com.sprint.project1.hrbank.repository.department.DepartmentRepository;
import com.sprint.project1.hrbank.repository.employee.EmployeeRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final EmployeeMapper employeeMapper;

  @Override
  public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
    Department department = departmentRepository.findById(request.departmentId())
        .orElseThrow(() -> new RuntimeException("Department not found"));

    if (employeeRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("Email already exists");
    }

    // profile 로직 추가

    Employee employee = employeeMapper.toEntity(request);
    employee.assignDepartment(department);
    employee.generateEmployeeNumber();
    Employee createdEmployee = employeeRepository.save(employee);
    return employeeMapper.toResponse(createdEmployee);
  }

  @Override
  public void deleteEmployee(Long employeeId) {
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new NoSuchElementException("Employee not found for id: " + employeeId));
    employeeRepository.delete(employee);
  }

  @Override
  public EmployeeResponse updateEmployee(Long employeeId, EmployeeUpdateRequest request) {
    Department department = departmentRepository.findById(request.departmentId())
        .orElseThrow(() -> new NoSuchElementException("Department not found for id: " + request.departmentId()));

    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new NoSuchElementException("Employee not found for id: " + employeeId));

    if (employeeRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("Email already exists");
    }

    // profile 로직 추가

    // 이메일 중복 안되게 하는 로직 추가 필요
    Employee updateEmployee = employee.update(request, department);
    Employee createdEmployee = employeeRepository.save(updateEmployee);
    return employeeMapper.toResponse(createdEmployee);
  }

  @Override
  public List<EmployeeTrendResponse> getEmployeeTrend(LocalDate from, LocalDate to, String unit) {
    List<LocalDate> datePoints = Employee.generateDatePointTrend(from, to, unit);
    List<EmployeeTrendResponse> employeeTrendResponses = new ArrayList<>();

    ZoneId zone = ZoneId.of("UTC");

    long preCount = 0;
    for (LocalDate date : datePoints) {
      Instant toInstant = date.plusDays(1).atStartOfDay().atZone(zone).toInstant();
      long count = employeeRepository.countEmployeesBefore(toInstant); // N+1 문제 발생됨
      long change = count - preCount;
      double changeRate = (preCount == 0) ? 0 : (double) change / preCount * 100;
      employeeTrendResponses.add(new EmployeeTrendResponse(date, count, change, changeRate));
      preCount = count;
    }
    return employeeTrendResponses;
  }

  @Override
  public List<EmployeeDistributionResponse> getEmployeeDistribution(String groupBy,
      EmployeeStatus status) {
    Long total = employeeRepository.countByStatus(status);
    List<Object[]> rawResult;

    if("department".equals(groupBy)){
      rawResult = employeeRepository.countByDepartment(status);
    }else if("position".equals(groupBy)){
      rawResult = employeeRepository.countByPosition(status);
    }else{
      return Collections.emptyList();
    }

    List<EmployeeDistributionResponse> result = new ArrayList<>();
    for(Object[] row : rawResult){
      String groupKey = (String) row[0];
      long count = (Long) row[1];
      double percentage = total > 0 ? (double) (count * 100) / total : 0.0;
      result.add(new EmployeeDistributionResponse(groupKey, count, percentage));
    }
    return result;
  }
}
