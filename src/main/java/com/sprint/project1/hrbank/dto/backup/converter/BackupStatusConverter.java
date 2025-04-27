package com.sprint.project1.hrbank.dto.backup.converter;

import com.sprint.project1.hrbank.entity.backup.BackupStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BackupStatusConverter implements Converter<String, BackupStatus> {

  @Override
  public BackupStatus convert(String source) {
    if (source == null || source.trim().isEmpty()) {
      return null;
    }
    try {
      return BackupStatus.valueOf(source.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
