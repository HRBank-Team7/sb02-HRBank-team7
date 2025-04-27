package com.sprint.project1.hrbank.mapper.log;

import com.sprint.project1.hrbank.dto.log.DiffCondition;
import com.sprint.project1.hrbank.entity.employee.Employee;
import com.sprint.project1.hrbank.entity.log.EmployeeChangeLog;
import com.sprint.project1.hrbank.entity.log.EmployeeDiff;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LogMapper {

  @Mapping(target = "employeeDiffs", source = "diffs")
  EmployeeChangeLog toEntity(
      List<EmployeeDiff> diffs,
      String employeeNumber,
      String type,
      String memo,
      String ipAddress,
      Instant at
  );
}
