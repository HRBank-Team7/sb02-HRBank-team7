package com.sprint.project1.hrbank.dto.log;

import java.util.List;

public record CursorPageResponseChangeLogResponse(
   List<ChangeLogResponse> content,
   String nextCursor,
   long nextIdAfter,
   int size,
   long totalElements,
   boolean hasNext
) {}
