package com.sprint.project1.hrbank.dto.log;

public record DiffResponse(
   String propertyName,
   String before,
   String after
) {}
