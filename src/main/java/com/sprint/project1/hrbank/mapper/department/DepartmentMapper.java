package com.sprint.project1.hrbank.mapper.department;

import com.sprint.project1.hrbank.dto.department.DepartmentResponse;
import com.sprint.project1.hrbank.entity.department.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper {

    @Mapping(target = "establishedDate", source = "department.establishedDate", qualifiedByName = "changeLocalDateInSeoul")
    DepartmentResponse toResponse(Department department, int employeeCount);


    @Named("changeLocalDateInSeoul")
    static LocalDate changeLocalDateInSeoul(Instant establishedDate) {
        return establishedDate.atZone(ZoneId.of("Asia/Seoul")).toLocalDate();
    }
}
