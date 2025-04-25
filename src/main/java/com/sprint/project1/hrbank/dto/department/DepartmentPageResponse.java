package com.sprint.project1.hrbank.dto.department;

import java.util.List;

public record DepartmentPageResponse(
    List<DepartmentResponse> content,
    String nextCursor,
    Long nextIdAfter,
    Integer size,
    boolean hasNext,
    Long totalElements
) {

}
