package com.sprint.project1.hrbank.dto.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepartmentSearchRequest(
    String nameOrDescription,
    Long idAfter,
    String cursor,
    Integer size,
    String sortField,
    String sortDirection
) {
  public DepartmentSearchRequest {
    if (size == null) {
      size = 10;
    }

    if (sortField == null) {
      sortField = "establishedDate";
    }

    if (sortDirection == null) {
      sortDirection = "asc";
    }
  }
}
