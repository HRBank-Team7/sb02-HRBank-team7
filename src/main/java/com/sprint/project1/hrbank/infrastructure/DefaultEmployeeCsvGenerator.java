package com.sprint.project1.hrbank.infrastructure;

import com.sprint.project1.hrbank.entity.employee.Employee;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultEmployeeCsvGenerator implements EmployeeCsvGenerator {

  @Override
  public byte[] generateCsvBytes(List<Employee> employees) throws IOException {
    StringBuilder sb = new StringBuilder("ID,직원번호,이름,이메일,부서,직급,입사일,상태\n");
    for (Employee e : employees) {
      sb.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s\n",
          e.getId(),
          e.getEmployeeNumber(),
          e.getName(),
          e.getEmail(),
          e.getDepartment() != null ? e.getDepartment().getName() : "",
          e.getPosition(),
          e.getHireDate(),
          e.getStatus().explain()
      ));
    }
    return sb.toString().getBytes("UTF-8");
  }
}
