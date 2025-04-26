package com.sprint.project1.hrbank.service.employee;

import com.sprint.project1.hrbank.dto.employee.CursorPageEmployeeResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeCreateRequest;
import com.sprint.project1.hrbank.dto.employee.EmployeeDistributionResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeSearchCondition;
import com.sprint.project1.hrbank.dto.employee.EmployeeSearchRequest;
import com.sprint.project1.hrbank.dto.employee.EmployeeTrendResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeUpdateRequest;
import com.sprint.project1.hrbank.dto.file.FileCreateRequest;
import com.sprint.project1.hrbank.entity.department.Department;
import com.sprint.project1.hrbank.entity.employee.Employee;
import com.sprint.project1.hrbank.entity.employee.EmployeeStatus;
import com.sprint.project1.hrbank.entity.file.File;
import com.sprint.project1.hrbank.exception.util.InvalidCursorFormatException;
import com.sprint.project1.hrbank.mapper.employee.EmployeeMapper;
import com.sprint.project1.hrbank.repository.department.DepartmentRepository;
import com.sprint.project1.hrbank.repository.employee.EmployeeRepository;
import com.sprint.project1.hrbank.service.file.FileService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final FileService fileService;
  private final EmployeeMapper employeeMapper;

  @Override
  @Transactional
  public EmployeeResponse createEmployee(EmployeeCreateRequest request, MultipartFile profile) {
    Department department = departmentRepository.findById(request.departmentId())
        .orElseThrow(() -> new RuntimeException("Department not found"));

    if (employeeRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("Email already exists");
    }

    File file = Optional.ofNullable(profile)
        .filter(p -> !p.isEmpty())
        .map(p -> {
          try {
            return fileService.create(new FileCreateRequest(
                p.getOriginalFilename(),
                p.getContentType(),
                (long) p.getBytes().length,
                p.getBytes()
            ));
          } catch (IOException e) {
            throw new RuntimeException("Failed to process profile file", e);
          }
        })
        .orElse(null);

    Employee employee = employeeMapper.toEntity(request);
    employee.assignDepartment(department);
    employee.assignFile(file);
    employee.generateEmployeeNumber();
    Employee createdEmployee = employeeRepository.save(employee);
    return employeeMapper.toResponse(createdEmployee);
  }

  @Override
  @Transactional
  public void deleteEmployee(Long employeeId) {
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new NoSuchElementException("Employee not found for id: " + employeeId));
    employeeRepository.delete(employee);
  }

  @Override
  @Transactional
  public EmployeeResponse updateEmployee(Long employeeId, EmployeeUpdateRequest request, MultipartFile profile) {
    Department department = departmentRepository.findById(request.departmentId())
        .orElseThrow(() -> new NoSuchElementException("Department not found for id: " + request.departmentId()));

    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new NoSuchElementException("Employee not found for id: " + employeeId));

    File file = Optional.ofNullable(profile)
        .filter(p -> !p.isEmpty())
        .map(p -> {
          try {
            return fileService.create(new FileCreateRequest(
                p.getOriginalFilename(),
                p.getContentType(),
                (long) p.getBytes().length,
                p.getBytes()
            ));
          } catch (IOException e) {
            throw new RuntimeException("Failed to process profile file", e);
          }
        })
        .orElse(null);

    if (!employee.getEmail().equals(request.email())) {
      if (employeeRepository.existsByEmail(request.email())){
        throw new IllegalArgumentException("Email already exists");}}

    Employee updateEmployee = employee.update(request, department, file);
    Employee createdEmployee = employeeRepository.save(updateEmployee);
    return employeeMapper.toResponse(createdEmployee);
  }

  @Override
  @Transactional(readOnly = true)
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
  @Transactional(readOnly = true)
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

  @Override
  @Transactional(readOnly = true)
  public Long getEmployeeByCount(EmployeeStatus status, LocalDate fromDate, LocalDate toDate) {
    if (fromDate == null) { // from 이 null 이고
      return (status == null) // status(재직 상태) 이 null 이면
          ? employeeRepository.count() // 재직 수 전체 return
          : employeeRepository.countAllEmployeesWithStatus(status);
          // status(재직 상태) 있다면 상태에 따른 직원 수 return
    }

    // toDate 없으면 default 현재 일시
    LocalDate endToDate = (toDate != null) ? toDate : LocalDate.now();

    return (status == null)
        ? employeeRepository.countEmployeesByHireDate(fromDate, endToDate)
        : employeeRepository.countEmployeesByHireDateWithStatus(status, fromDate, endToDate);
  }

  @Override
  @Transactional(readOnly = true)
  public CursorPageEmployeeResponse getAllEmployee(EmployeeSearchRequest request) {
    //sortField의 기본값은 무조건 name
    String sortField = (List.of("hireDate", "employeeNumber").contains(request.sortField())) ? request.sortField() : "name";
    String sortDirection = "desc".equals(request.sortDirection()) ? request.sortDirection() : "asc";
    String cursor = (request.cursor() != null) ? decodeCursor(request.cursor()) : null;

    if(cursor != null){
      checkCursorDateFormat(sortField, cursor);
    }

    int size = (request.size() == null || request.size() == 0) ? 10 : request.size();
    Pageable pageable = PageRequest.of(0, size + 1);

    EmployeeSearchCondition condition = employeeMapper.toCondition(request, pageable, cursor);
    List<Employee> employees = employeeRepository.searchEmployees(condition);

    boolean hasNext = employees.size() > size;
    List<Employee> content = hasNext ? employees.subList(0, size) : employees;

    long totalElements = employeeRepository.count();

    Employee lastEmployee = hasNext ? content.get(size-1) : null;
    Long nextIdAfter = hasNext ? lastEmployee.getId() : null;
    String nextCursor = hasNext ? extractCursor(lastEmployee, request) : null;

    List<EmployeeResponse> response = content.stream().map(employeeMapper::toResponse).toList();

    return new CursorPageEmployeeResponse(
        response,
        nextCursor,
        nextIdAfter,
        size,
        totalElements,
        hasNext
    );
  }

  public EmployeeResponse getEmployeeById(Long employeeId) {
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new NoSuchElementException("Employee not found for id: " + employeeId));
    return employeeMapper.toResponse(employee);
  }

  private void checkCursorDateFormat(String sortField, String cursor) {
    if (sortField.equalsIgnoreCase("hireDate")) {
      try {
        LocalDate.parse(cursor, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      } catch (DateTimeParseException ex) {
        throw new InvalidCursorFormatException("잘못된 커서 형식입니다.");
      }
    }
  }

  private String decodeCursor(String cursor) {
    try {
      return new String(Base64.getDecoder().decode(cursor), StandardCharsets.UTF_8);
    }catch(IllegalArgumentException ex){
      throw new InvalidCursorFormatException("잘못된 커서 형식입니다.");
    }
  }

  private String extractCursor(Employee lastEmployee, EmployeeSearchRequest request) {
    String extractedCursor = switch(request.sortField()) {
      case "hireDate" -> lastEmployee.getHireDate().toString();
      case "employeeNumber" -> lastEmployee.getEmployeeNumber();
      default -> lastEmployee.getName();
    };

    return Base64.getEncoder().encodeToString(extractedCursor.getBytes(StandardCharsets.UTF_8));
  }
}
