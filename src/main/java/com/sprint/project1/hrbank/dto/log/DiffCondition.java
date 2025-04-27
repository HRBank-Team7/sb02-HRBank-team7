package com.sprint.project1.hrbank.dto.log;

import com.sprint.project1.hrbank.entity.employee.EmployeeStatus;
import java.time.LocalDate;

public record DiffCondition(
   LocalDate hireDate,
   String name,
   String position,
   String departmentName,
   String email,
   EmployeeStatus status
) {}
