package com.sprint.project1.hrbank.dto.log;

import java.time.Instant;
import org.springframework.data.domain.Pageable;

public record EmployeeLogSearchRequest(
    String type,
    String employeeNumber,
    String memo,
    String ipAddress,
    Instant atFrom,
    Instant atTo,
    Long idAfter,
    String cursor,
    Integer size,
    String sortField,
    String sortDirection
) {
  public EmployeeLogSearchRequest {
        if(size == null || size == 0){
          size = 10;
        }
        if(sortField == null){
          sortField = "at";
        }
        if(sortDirection == null){
          sortDirection = "desc";
        }
  }
}
