package com.sprint.project1.hrbank.dto.log;

import java.time.Instant;

public record ChangeLogResponse(
   long id,
   String type,
   String employeeNumber,
   String memo,
   String ipAddress,
   Instant at
) {}
