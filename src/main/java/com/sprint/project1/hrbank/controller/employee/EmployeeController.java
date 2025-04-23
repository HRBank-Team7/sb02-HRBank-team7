package com.sprint.project1.hrbank.controller.employee;

import com.sprint.project1.hrbank.dto.employee.EmployeeCreateRequest;
import com.sprint.project1.hrbank.dto.employee.EmployeeResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeUpdateRequest;
import com.sprint.project1.hrbank.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
      @RequestPart(value = "employee") EmployeeCreateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    EmployeeResponse employeeResponseResponse = employeeService.createEmployee(request);
    return ResponseEntity.ok(employeeResponseResponse);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(
      @PathVariable Long id
  ) {
    employeeService.deleteEmployee(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}")
  public ResponseEntity<EmployeeResponse> updateEmployee(
      @PathVariable Long id,
      @RequestPart(value = "employee") EmployeeUpdateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile //파일 나중에 처리 해야함
  ) {
    EmployeeResponse employeeResponse = employeeService.updateEmployee(id, request /*,profile*/);
    return ResponseEntity.ok(employeeResponse);
  }
}
