package com.sprint.project1.hrbank.dto.employee;

import com.sprint.project1.hrbank.entity.employee.EmployeeStatus;
import java.time.Instant;

public record EmployeeUpdateRequest(
    String name,
    String email,
    Long departmentId,
    String position,
    Instant hireDate,
    EmployeeStatus status,
    String memo
) {}
