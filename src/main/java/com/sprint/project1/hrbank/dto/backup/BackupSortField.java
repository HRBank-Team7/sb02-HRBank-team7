package com.sprint.project1.hrbank.dto.backup;

public enum BackupSortField {
  STARTED_AT,
  ENDED_AT;

  public static BackupSortField from(String value) {
    if (value == null) {
      return null;
    }
    return switch (value.toUpperCase()) {
      case "STARTED_AT" -> STARTED_AT;
      case "ENDED_AT" -> ENDED_AT;
      default -> throw new IllegalArgumentException("지원하지 않는 정렬 필드: " + value);
    };
  }
}
