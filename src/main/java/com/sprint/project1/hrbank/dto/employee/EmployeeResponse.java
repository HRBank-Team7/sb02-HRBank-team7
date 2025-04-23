package com.sprint.project1.hrbank.dto.employee;

import java.time.LocalDate;

public record EmployeeResponse(
    Long id,
    String name,
    String email,
    String employeeNumber,
    Long departmentId,
    String departmentName,
    String position,
    LocalDate hireDate,
    String status,
    Long profileImageId
) {}
