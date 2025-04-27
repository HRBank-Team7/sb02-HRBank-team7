package com.sprint.project1.hrbank.repository.log;

import com.sprint.project1.hrbank.dto.log.ChangeLogResponse;
import com.sprint.project1.hrbank.dto.log.EmployeeLogCondition;
import com.sprint.project1.hrbank.entity.log.EmployeeLog;
import java.time.Instant;
import java.util.List;

public interface EmployeeLogRepositoryCustom {
  List<EmployeeLog> searchEmployeeLogs(EmployeeLogCondition condition);
  Long getCountByDate(Instant fromDate, Instant toDate);
}
