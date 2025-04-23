package com.sprint.project1.hrbank.mapper.file;

import com.sprint.project1.hrbank.dto.file.FileMetadata;
import com.sprint.project1.hrbank.entity.file.File;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {

    FileMetadata toMetadata(File file);
}
