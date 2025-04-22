package com.sprint.project1.hrbank.dto.employee;

import com.sprint.project1.hrbank.entity.employee.EmployeeStatus;
import java.time.Instant;

public record EmployeeDto(
    Long id,
    String name,
    String email,
    String employeeNumber,
    Long departmentId,
    String departmentName,
    String position,
    Instant hireDate,
    EmployeeStatus status,
    Long profileImageId
) {}
