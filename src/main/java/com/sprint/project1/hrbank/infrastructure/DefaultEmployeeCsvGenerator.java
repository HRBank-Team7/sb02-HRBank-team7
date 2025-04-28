package com.sprint.project1.hrbank.infrastructure;

import com.sprint.project1.hrbank.entity.employee.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultEmployeeCsvGenerator implements EmployeeCsvGenerator {

  private static final byte[] UTF8_BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
  //TODO: 데이터량 증가 시 스트리밍 파일 작성 검토
  @Override
  public byte[] generateCsvBytes(Stream<Employee> employees) throws IOException {
    StringBuilder sb = new StringBuilder("ID,직원번호,이름,이메일,부서,직급,입사일,상태\n");
    employees.forEach(e -> appendEmployee(sb, e));

    byte[] contentBytes = sb.toString().getBytes("UTF-8");

    // 엑셀 열때 한글 깨짐 방지
    byte[] result = new byte[UTF8_BOM.length + contentBytes.length];
    System.arraycopy(UTF8_BOM, 0, result, 0, UTF8_BOM.length);
    System.arraycopy(contentBytes, 0, result, UTF8_BOM.length, contentBytes.length);

    return result;
  }

  private void appendEmployee(StringBuilder sb, Employee e) {
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
}
