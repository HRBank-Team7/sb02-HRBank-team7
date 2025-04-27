package com.sprint.project1.hrbank.dto.employee;

import com.sprint.project1.hrbank.entity.employee.EmployeeStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record EmployeeUpdateRequest(
    @NotBlank String name,
    @NotBlank @Email String email,
    @NotNull Long departmentId,
    @NotNull String position,
    @NotNull LocalDate hireDate,
    @NotNull EmployeeStatus status,
    String memo
) {}
