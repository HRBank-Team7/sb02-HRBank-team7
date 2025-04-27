package com.sprint.project1.hrbank.dto.backup.converter;

import com.sprint.project1.hrbank.dto.backup.BackupSortDirection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BackupSortDirectionConverter implements Converter<String, BackupSortDirection> {

  @Override
  public BackupSortDirection convert(String source) {
    if (source == null || source.trim().isEmpty()) {
      return null;
    }
    try {
      return BackupSortDirection.valueOf(source.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
