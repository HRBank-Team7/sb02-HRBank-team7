package com.sprint.project1.hrbank.infrastructure;

import com.sprint.project1.hrbank.entity.employee.Employee;
import java.io.IOException;
import java.util.List;

public interface EmployeeCsvGenerator {
  byte[] generateCsvBytes(List<Employee> employees) throws IOException;
}
