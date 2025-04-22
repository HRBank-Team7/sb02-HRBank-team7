package com.sprint.project1.hrbank.dto.employee;

import java.time.Instant;

public record EmployeeCreateRequest(
    String name,
    String email,
    Long departmentId,
    String position,
    Instant hireDate,
    String memo
) {
}
