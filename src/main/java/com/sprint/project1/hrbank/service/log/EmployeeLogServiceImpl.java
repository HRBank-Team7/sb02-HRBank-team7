package com.sprint.project1.hrbank.service.log;

import com.sprint.project1.hrbank.entity.employee.Employee;
import com.sprint.project1.hrbank.entity.log.EmployeeChangeLog;
import com.sprint.project1.hrbank.entity.log.EmployeeDiff;
import com.sprint.project1.hrbank.repository.log.EmployeeLogRepository;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class EmployeeLogServiceImpl implements EmployeeLogService{

  private final EmployeeLogRepository employeeLogRepository;

  @Override
  public void createLog(Employee before, Employee after, String inputMemo, String ipAddress) {
    String type = getType(before, after);
    String employNumber = getEmployNumber(before, after, type);
    String memo = setMemo(type, inputMemo);
    List<String> properties = List.of("hireDate", "name", "position", "department", "email", "status");
    List<EmployeeDiff> diffs = processEmployeeDiffs(before, after, properties);
    Instant at = Instant.now();

    EmployeeChangeLog employeeChangeLog =
        new EmployeeChangeLog (
            type,
            employNumber,
            diffs,
            memo,
            ipAddress,
            at
        );

    employeeLogRepository.save(employeeChangeLog);
  }

  private String getEmployNumber(Employee before, Employee after, String type) {
    return switch (type) {
      case "CREATED" -> after.getEmployeeNumber();
      case "UPDATED" -> before.getEmployeeNumber();
      case "DELETED" -> before.getEmployeeNumber();
      default -> throw new IllegalStateException("잘못된 타입입니다.");
    };
  }

  private String getType(Employee before, Employee after) {
    if (before == null && after != null) {
      return "CREATED";
    } else if (before != null && after != null) {
      return "UPDATED";
    } else if (before != null && after == null) {
      return "DELETED";
    } else {
      throw new IllegalArgumentException("입력값이 잘못 들어왔습니다.");
    }
  }

  private List<EmployeeDiff> processEmployeeDiffs(Employee before, Employee after,
      List<String> properties) {
    List<EmployeeDiff> diffs = properties.stream()
        .map(property -> {
          String beforeValue = getPropertyValue(before, property);
          String afterValue = getPropertyValue(after, property);
          return (!Objects.equals(beforeValue, afterValue)) ? new EmployeeDiff(property, beforeValue, afterValue) : null;
        })
        .filter(Objects::nonNull)
        .toList();
    return diffs;
  }

  private static String getPropertyValue(Employee employee, String propertyName) {
    if (employee == null) {
      return null;
    }
    return switch (propertyName) {
      case "hireDate" -> employee.getHireDate().toString();
      case "name" -> employee.getName();
      case "position" -> employee.getPosition();
      case "department" -> employee.getDepartment() != null ? employee.getDepartment().getName()
          : null; // Department 이름 반환
      case "email" -> employee.getEmail();
      case "status" -> employee.getStatus().toString();
      default -> null;
    };
  }

  private static String setMemo(String type, String inputMemo) {
    if (inputMemo != null) {
      return inputMemo;
    }
    return switch (type) {
      case "CREATED" -> "신규 직원 등록";
      case "UPDATED" -> "직원 정보 수정";
      case "DELETED" -> "직원 삭제";
      default -> throw new IllegalStateException("잘못된 타입입니다.");
    };
  }
}
