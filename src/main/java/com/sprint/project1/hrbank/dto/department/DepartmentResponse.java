package com.sprint.project1.hrbank.dto.department;

import java.time.LocalDate;

public record DepartmentResponse(
        long id,
        String name,
        String description,
        LocalDate establishedDate,
        int employeeCount
) {
}
