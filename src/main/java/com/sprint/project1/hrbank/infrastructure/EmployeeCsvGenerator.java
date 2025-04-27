package com.sprint.project1.hrbank.infrastructure;

import com.sprint.project1.hrbank.entity.employee.Employee;
import java.io.IOException;
import java.util.stream.Stream;

public interface EmployeeCsvGenerator {
  byte[] generateCsvBytes(Stream<Employee> employeeStream) throws IOException;
}
