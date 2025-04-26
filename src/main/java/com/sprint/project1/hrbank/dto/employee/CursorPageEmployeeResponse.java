package com.sprint.project1.hrbank.dto.employee;

import java.util.List;

public record CursorPageEmployeeResponse(
    List<EmployeeResponse> content,
    String nextCursor,
    Long nextIdAfter,
    int size,
    long totalElements,
    boolean hasNext
) {

}
