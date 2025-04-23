package com.sprint.project1.hrbank.dto.department;

import java.time.Instant;

public record DepartmentUpdateRequest(
        String name,
        String description,
        Instant establishedDate
) {
}