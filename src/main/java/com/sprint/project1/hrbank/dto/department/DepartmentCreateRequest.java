package com.sprint.project1.hrbank.dto.department;

import java.time.Instant;

public record DepartmentCreateRequest (
        String name,
        String description,
        Instant establishedDate
){}
