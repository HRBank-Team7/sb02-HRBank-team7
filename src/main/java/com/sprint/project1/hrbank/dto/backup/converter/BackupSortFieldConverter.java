package com.sprint.project1.hrbank.dto.backup.converter;

import com.sprint.project1.hrbank.dto.backup.BackupSortField;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BackupSortFieldConverter implements Converter<String, BackupSortField> {

  @Override
  public BackupSortField convert(String source) {
    if (source == null || source.trim().isEmpty()) {
      return null;
    }
    try {
      return BackupSortField.valueOf(source.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
