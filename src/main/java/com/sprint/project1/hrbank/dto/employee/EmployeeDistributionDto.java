package com.sprint.project1.hrbank.dto.employee;

public record EmployeeDistributionDto(
   String groupKey,
   Long count,
   double percentage
) {}
