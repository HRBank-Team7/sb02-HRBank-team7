package com.sprint.project1.hrbank.service.log;

import com.sprint.project1.hrbank.dto.log.CursorPageChangeLogResponse;
import com.sprint.project1.hrbank.dto.log.DiffResponse;
import com.sprint.project1.hrbank.dto.log.DiffsResponse;
import com.sprint.project1.hrbank.dto.log.EmployeeLogSearchRequest;
import com.sprint.project1.hrbank.entity.employee.Employee;
import java.time.Instant;
import java.util.List;

public interface EmployeeLogService {
  void createLog(Employee before, Employee after, String memo, String ipAddress);

  List<DiffResponse> getById(Long id);

  CursorPageChangeLogResponse getAllChangeLog(EmployeeLogSearchRequest request);

  Long getEmployeeLogCount(Instant fromDate, Instant toDate);
}
