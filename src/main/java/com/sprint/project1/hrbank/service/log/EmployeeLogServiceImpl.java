package com.sprint.project1.hrbank.service.log;

import com.sprint.project1.hrbank.dto.log.ChangeLogResponse;
import com.sprint.project1.hrbank.dto.log.CursorPageChangeLogResponse;
import com.sprint.project1.hrbank.dto.log.DiffResponse;
import com.sprint.project1.hrbank.dto.log.EmployeeLogCondition;
import com.sprint.project1.hrbank.dto.log.EmployeeLogSearchRequest;
import com.sprint.project1.hrbank.entity.employee.Employee;
import com.sprint.project1.hrbank.entity.log.EmployeeDiff;
import com.sprint.project1.hrbank.entity.log.EmployeeLog;
import com.sprint.project1.hrbank.exception.log.EmployeeLogNotFoundException;
import com.sprint.project1.hrbank.exception.util.InvalidCursorFormatException;
import com.sprint.project1.hrbank.mapper.log.LogMapper;
import com.sprint.project1.hrbank.repository.log.EmployeeLogRepository;
import com.sprint.project1.hrbank.util.CursorManager;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class EmployeeLogServiceImpl implements EmployeeLogService{

  private final EmployeeLogRepository employeeLogRepository;
  private final LogMapper logMapper;

  @Override
  @Transactional
  public void createLog(Employee before, Employee after, String inputMemo, String ipAddress) {
    String type = getType(before, after);
    String employNumber = getEmployNumber(before, after, type);
    String memo = setMemo(type, inputMemo);
    List<String> properties = List.of("hireDate", "name", "position", "department", "email", "status");
    List<EmployeeDiff> diffs = processEmployeeDiffs(before, after, properties);
    Instant at = Instant.now();

    EmployeeLog employeeLog =
        new EmployeeLog(
            type,
            employNumber,
            diffs,
            memo,
            ipAddress,
            at
        );

    employeeLogRepository.save(employeeLog);
  }

  @Override
  @Transactional(readOnly = true)
  public List<DiffResponse> getById(Long id) {
    EmployeeLog employeeLog = employeeLogRepository.findById(id).orElseThrow(
        () -> new EmployeeLogNotFoundException("직원 정보 이력이 없습니다."));

    return employeeLog.getEmployeeDiffs().stream()
        .map(logMapper::toResponse)
        .toList();
  }


  @Override
  @Transactional
  public CursorPageChangeLogResponse getAllChangeLog(EmployeeLogSearchRequest request) {
//    sortField 기본 값 at, sortDirection 기본 값 desc
    String sortField = Objects.equals(request.sortField(), "ipAddress") ? "ipAddress" : "at";
    String sortDirection = Objects.equals(request.sortDirection(), "asc") ? "asc" : "desc";
    String cursor = (request.cursor() != null)
        ? CursorManager.decodeCursorToString(request.cursor())
        : null;

    if(cursor != null){
      checkCursorDateFormat(sortField, cursor);
    }

    int size = (request.size() == null || request.size() == 0) ? 10 : request.size();
    Pageable pageable = PageRequest.of(0, size + 1);

    EmployeeLogCondition condition = logMapper.toCondition(request, pageable, cursor, sortField, sortDirection);

    List<EmployeeLog> employeeLogs = employeeLogRepository.searchEmployeeLogs(condition);

    boolean hasNext = employeeLogs.size() > size;
    List<EmployeeLog> content = hasNext ? employeeLogs.subList(0, size) : employeeLogs;

    long totalElements = employeeLogRepository.count();

    EmployeeLog lastEmployeeLog = hasNext ? content.get(size-1) : null;
    Long nextIdAfter = hasNext ? lastEmployeeLog.getId() : null;
    String nextCursor = hasNext ? extractCursor(lastEmployeeLog, request) : null;

    List<ChangeLogResponse> response = content.stream().map(logMapper::toResponse).toList();

    return new CursorPageChangeLogResponse(
        response,
        nextCursor,
        nextIdAfter,
        size,
        totalElements,
        hasNext
    );
  }

  @Override
  @Transactional(readOnly = true)
  public Long getEmployeeLogCount(Instant fromDate, Instant toDate) {
    return employeeLogRepository.getCountByDate(fromDate, toDate);
  }

  private String extractCursor(EmployeeLog lastEmployeeLog, EmployeeLogSearchRequest request) {
    return "ipAddress".equals(request.sortField())
        ? lastEmployeeLog.getIpAddress()
        : lastEmployeeLog.getAt().toString();

  }

  private void checkCursorDateFormat(String sortField, String cursor) {
    if("at".equalsIgnoreCase(sortField)){
      try{
        Instant.parse(cursor);
      }catch(DateTimeParseException ex){
        throw new InvalidCursorFormatException("잘못된 커서 형식입니다.");
      }
    }
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
