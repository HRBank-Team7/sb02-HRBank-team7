package com.sprint.project1.hrbank.dto.department;

import java.time.LocalDate;

public record DepartmentUpdateRequest(
        String name,
        String description,
        LocalDate establishedDate
) {
}