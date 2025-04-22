package com.sprint.project1.hrbank.entity.log;

public record EmployeeDiff(
    String propertyName,
    String before,
    String after
) {

}
