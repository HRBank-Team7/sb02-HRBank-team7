package com.sprint.project1.hrbank.dto.employee;

import com.sprint.project1.hrbank.entity.employee.EmployeeStatus;
import java.time.LocalDate;

public record EmployeeSearchRequest(
    String nameOrEmail,
    String departmentName,
    String position,
    String employeeNumber,
    LocalDate hireDateFrom,
    LocalDate hireDateTo,
    EmployeeStatus status,
    Long idAfter,
    String cursor,
    String sortField,
    String sortDirection,
    Integer size
){
  public EmployeeSearchRequest {
    if (size == null || size == 0) {
      size = 10;
    }
    if (sortField == null) {
      sortField = "name";
    }
    if (sortDirection == null) {
      sortDirection = "asc";
    }
  }

}
