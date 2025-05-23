package com.sprint.project1.hrbank.controller.employee;

import com.sprint.project1.hrbank.dto.employee.CursorPageEmployeeResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeCreateRequest;
import com.sprint.project1.hrbank.dto.employee.EmployeeDistributionResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeSearchRequest;
import com.sprint.project1.hrbank.dto.employee.EmployeeTrendResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeUpdateRequest;
import com.sprint.project1.hrbank.entity.employee.EmployeeStatus;
import com.sprint.project1.hrbank.service.employee.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public  class EmployeeController {

  private final EmployeeService employeeService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EmployeeResponse> createEmployee(
      @Valid @RequestPart(value = "employee") EmployeeCreateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile,
      HttpServletRequest httpServletRequest
  ) {
    String ip = httpServletRequest.getRemoteAddr();
    EmployeeResponse employeeResponseResponse = employeeService.createEmployee(request, profile, ip);
    return ResponseEntity.ok(employeeResponseResponse);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(
      @PathVariable Long id,
      HttpServletRequest httpServletRequest
  ) {
    String ip = httpServletRequest.getRemoteAddr();
    employeeService.deleteEmployee(id, ip);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}")
  public ResponseEntity<EmployeeResponse> updateEmployee(
      @PathVariable Long id,
      @Valid @RequestPart(value = "employee") EmployeeUpdateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile,
      HttpServletRequest httpServletRequest
  ) {
    String ip = httpServletRequest.getRemoteAddr();
    EmployeeResponse employeeResponse = employeeService.updateEmployee(id, request, profile, ip);
    return ResponseEntity.ok(employeeResponse);
  }
  
  @GetMapping("/stats/trend")
  public ResponseEntity<List<EmployeeTrendResponse>> getEmployeeTrend(
      @RequestParam(required = false) LocalDate from,
      @RequestParam(required = false) LocalDate to,
      @RequestParam(required = false, defaultValue = "month") String unit
  ){
    List<EmployeeTrendResponse> employeeTrendResponse = employeeService.getEmployeeTrend(from, to, unit);
    return ResponseEntity.ok(employeeTrendResponse);
  }

  @GetMapping("/stats/distribution")
  public ResponseEntity<List<EmployeeDistributionResponse>> getEmployeeDistribution(
      @RequestParam(defaultValue = "department") String groupBy,
      @RequestParam(defaultValue = "ACTIVE") EmployeeStatus status
  ) {
    List<EmployeeDistributionResponse> response =
        employeeService.getEmployeeDistribution(groupBy, status);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/count")
  public ResponseEntity<Long> getEmployeeCount(
      @RequestParam(required = false) EmployeeStatus status,
      @RequestParam(required = false) LocalDate fromDate,
      @RequestParam(required = false) LocalDate toDate
  ){
    Long employeeCount = employeeService.getEmployeeByCount(status, fromDate, toDate);
    return ResponseEntity.ok(employeeCount);
  }

  @GetMapping
  public ResponseEntity<CursorPageEmployeeResponse> getEmployees(
      @ModelAttribute EmployeeSearchRequest request
  ){
    CursorPageEmployeeResponse response = employeeService.getAllEmployee(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmployeeResponse> getEmployeeById(
      @PathVariable Long id
  ){
      EmployeeResponse response = employeeService.getEmployeeById(id);
      return ResponseEntity.ok(response);
  }
}
