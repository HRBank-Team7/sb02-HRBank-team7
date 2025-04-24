package com.sprint.project1.hrbank.mapper.employee;

import com.sprint.project1.hrbank.dto.employee.EmployeeCreateRequest;
import com.sprint.project1.hrbank.dto.employee.EmployeeResponse;
import com.sprint.project1.hrbank.entity.employee.Employee;
import com.sprint.project1.hrbank.entity.employee.EmployeeStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

  @Mapping(target = "status", source = "status", qualifiedByName = "explainStatus")
  @Mapping(target = "departmentId", source = "department.id")
  @Mapping(target = "departmentName", source = "department.name")
  @Mapping(target = "profileImageId", source = "file.id")
  EmployeeResponse toResponse(Employee entity);

  Employee toEntity(EmployeeResponse response);

  @Mapping(target = "status", expression = "java(EmployeeStatus.ACTIVE)")
  @Mapping(target = "file", ignore = true)
  Employee toEntity(EmployeeCreateRequest request);

  @Named("explainStatus")
  static String toKorean(EmployeeStatus status) {
    return EmployeeStatus.explainStatus(status);
  }

}
