package com.sprint.project1.hrbank.dto.log;

import java.time.Instant;
import org.springframework.data.domain.Pageable;

public record EmployeeLogCondition(
    String type,
    String employeeNumber,
    String memo,
    String ipAddress,
    Instant atFrom,
    Instant atTo,
    Long idAfter,
    String cursor,
    String sortField,
    String sortDirection,
    Pageable pageable
) {}


