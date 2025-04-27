package com.sprint.project1.hrbank.dto.backup;

public enum BackupSortDirection {
  ASC,
  DESC;

  public static BackupSortDirection from(String value) {
    if (value == null) {
      return null;
    }
    return switch (value.toUpperCase()) {
      case "ASC" -> ASC;
      case "DESC" -> DESC;
      default -> throw new IllegalArgumentException("지원하지 않는 정렬 방향: " + value);
    };
  }
}
