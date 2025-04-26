package com.sprint.project1.hrbank.dto.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record DepartmentUpdateRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull LocalDate establishedDate
) {
}