package com.sprint.project1.hrbank.service.employee;

import com.sprint.project1.hrbank.dto.employee.EmployeeCreateRequest;
import com.sprint.project1.hrbank.dto.employee.EmployeeResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeUpdateRequest;
import com.sprint.project1.hrbank.entity.department.Department;
import com.sprint.project1.hrbank.entity.employee.Employee;
import com.sprint.project1.hrbank.mapper.employee.EmployeeMapper;
import com.sprint.project1.hrbank.repository.department.DepartmentRepository;
import com.sprint.project1.hrbank.repository.employee.EmployeeRepository;
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

    Employee updateEmployee = employee.update(request, department);
    Employee createdEmployee = employeeRepository.save(updateEmployee);
    return employeeMapper.toResponse(createdEmployee);
  }


}
