package com.sprint.project1.hrbank.controller.log;

import com.sprint.project1.hrbank.dto.log.CursorPageChangeLogResponse;
import com.sprint.project1.hrbank.dto.log.DiffResponse;
import com.sprint.project1.hrbank.dto.log.EmployeeLogSearchRequest;
import com.sprint.project1.hrbank.service.log.EmployeeLogService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/change-logs")
public class EmployeeLogController {

  private final EmployeeLogService employeeLogService;

  @GetMapping
  public ResponseEntity<CursorPageChangeLogResponse> getEmployeeLogs(
      @ModelAttribute EmployeeLogSearchRequest request
  ) {
    CursorPageChangeLogResponse response = employeeLogService.getAllChangeLog(request);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}/diffs")
  public ResponseEntity<List<DiffResponse>> getById(@PathVariable Long id) {
    List<DiffResponse> diffs = employeeLogService.getById(id);

    return ResponseEntity.ok(diffs);
  }

  @GetMapping("/count")
  public ResponseEntity<Long> getEmployeeLogCount(
      @RequestParam Instant fromDate,
      @RequestParam Instant toDate
  ){
    Long employeeLogCount = employeeLogService.getEmployeeLogCount(fromDate, toDate);
    return ResponseEntity.ok(employeeLogCount);
  }
}
