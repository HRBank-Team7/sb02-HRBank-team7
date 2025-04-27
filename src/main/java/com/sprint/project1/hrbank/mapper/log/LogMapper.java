package com.sprint.project1.hrbank.mapper.log;

import com.sprint.project1.hrbank.dto.log.ChangeLogResponse;
import com.sprint.project1.hrbank.dto.log.DiffResponse;
import com.sprint.project1.hrbank.dto.log.EmployeeLogCondition;
import com.sprint.project1.hrbank.dto.log.EmployeeLogSearchRequest;
import com.sprint.project1.hrbank.entity.log.EmployeeDiff;
import com.sprint.project1.hrbank.entity.log.EmployeeLog;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Pageable;

@Mapper(componentModel = "spring")
public interface LogMapper {

//  @Mapping(target = "employeeDiffs", source = "diffs")
//  EmployeeLog toEntity(
//      List<EmployeeDiff> diffs,
//      String employeeNumber,
//      String type,
//      String memo,
//      String ipAddress,
//      Instant at
//  );

  DiffResponse toResponse(EmployeeDiff diff);

  ChangeLogResponse toResponse(EmployeeLog log);

  default EmployeeLogCondition toCondition(
      EmployeeLogSearchRequest request,
      Pageable pageable,
      String cursor,
      String sortField,
      String sortDirection
  ){
    return new EmployeeLogCondition(
      request.type(),
      request.employeeNumber(),
      request.memo(),
      request.ipAddress(),
      request.atFrom(),
      request.atTo(),
      request.idAfter(),
      cursor,
      sortField,
      sortDirection,
      pageable
    );
  }
}
