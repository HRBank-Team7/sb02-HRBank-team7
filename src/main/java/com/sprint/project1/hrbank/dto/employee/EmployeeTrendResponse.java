package com.sprint.project1.hrbank.dto.employee;

import java.time.LocalDate;

public record EmployeeTrendResponse(
    LocalDate date,
    Long count,
    Long change,
    Double changeRate
) {

}
