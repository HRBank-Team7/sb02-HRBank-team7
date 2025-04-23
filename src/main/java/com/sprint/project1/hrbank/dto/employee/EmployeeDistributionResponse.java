package com.sprint.project1.hrbank.dto.employee;

public record EmployeeDistributionResponse(
   String groupKey,
   Long count,
   double percentage
) {}
