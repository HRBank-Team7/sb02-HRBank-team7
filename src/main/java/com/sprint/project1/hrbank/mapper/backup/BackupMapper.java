package com.sprint.project1.hrbank.mapper.backup;

import com.sprint.project1.hrbank.dto.backup.BackupResponse;
import com.sprint.project1.hrbank.entity.backup.Backup;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BackupMapper {
  @Mapping(target = "fileId", source = "file.id")
  @Mapping(target = "fileName", source = "file.name")
  @Mapping(target = "status", source = "status")
  BackupResponse toDto(Backup entity);
}
