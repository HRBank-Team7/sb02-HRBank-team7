package com.sprint.project1.hrbank.dto.employee;

public record EmployeeTrendDto(
    String date,
    Long count,
    Long change,
    Double changeRate
) {

}
