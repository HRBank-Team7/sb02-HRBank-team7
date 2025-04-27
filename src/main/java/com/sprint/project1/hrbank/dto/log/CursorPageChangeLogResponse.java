package com.sprint.project1.hrbank.dto.log;

import java.util.List;

public record CursorPageChangeLogResponse(
   List<ChangeLogResponse> content,
   String nextCursor,
   Long nextIdAfter,
   int size,
   long totalElements,
   boolean hasNext
) {}
