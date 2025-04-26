package com.sprint.project1.hrbank.mapper.employee;

import com.sprint.project1.hrbank.dto.employee.CursorPageEmployeeResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeCreateRequest;
import com.sprint.project1.hrbank.dto.employee.EmployeeResponse;
import com.sprint.project1.hrbank.dto.employee.EmployeeSearchCondition;
import com.sprint.project1.hrbank.dto.employee.EmployeeSearchRequest;
import com.sprint.project1.hrbank.entity.employee.Employee;
import com.sprint.project1.hrbank.entity.employee.EmployeeStatus;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

  // Employee entity -> EmployeeResponse
//  @Mapping(target = "status", source = "status", qualifiedByName = "explainStatus")
  @Mapping(target = "departmentId", source = "department.id")
  @Mapping(target = "departmentName", source = "department.name")
  @Mapping(target = "profileImageId", source = "file.id")
  EmployeeResponse toResponse(Employee entity);

//  @Named("explainStatus")
//  static String toKorean(EmployeeStatus status) {
//    return EmployeeStatus.explainStatus(status);
//  }

  // EmployeeResponse -> Employee entity
  Employee toEntity(EmployeeResponse response);

  // EmployeeCreateRequest -> Employee entity
  @Mapping(target = "status", expression = "java(EmployeeStatus.ACTIVE)")
  @Mapping(target = "file", ignore = true)
  Employee toEntity(EmployeeCreateRequest request);

  // Page<EmployeeResponse> -> CursorPageEmployeeResponse
  default CursorPageEmployeeResponse toResponse(Page<EmployeeResponse> page) {
    List<EmployeeResponse> content = page.getContent();
    EmployeeResponse lastContent =
        page.hasNext() && !content.isEmpty() ? content.get(content.size() - 1) : null;

    return new CursorPageEmployeeResponse(
        content,
        lastContent != null ? lastContent.name() : null,
        lastContent != null ? lastContent.id() : null,
        page.getSize(),
        page.getTotalElements(),
        page.hasNext()
    );
  }

  default Page<EmployeeResponse> toResponsePage(
      List<Employee> employees,
      Pageable pageable,
      Long total
  ) {
    List<EmployeeResponse> content = employees.stream()
        .map(this::toResponse)
        .toList();

    return new PageImpl<>(
        content,
        pageable,
        total);
  }

  // cursor decode 코드
  @Mapping(target = "cursor", source = "cursor", qualifiedByName = "cursorDecode")
  EmployeeSearchRequest employeeCursorDecode(EmployeeSearchRequest request);

  @Named("cursorDecode")
  static String cursorDecode(String cursor) {
    if (cursor != null) {
      return URLDecoder.decode(cursor, StandardCharsets.UTF_8);
    }
    return null;
  }

  default EmployeeSearchCondition toCondition(
      EmployeeSearchRequest request,
      Pageable pageable,
      String cursor){
    return new EmployeeSearchCondition(
        request.nameOrEmail(),
        request.departmentName(),
        request.position(),
        request.employeeNumber(),
        request.hireDateFrom(),
        request.hireDateTo(),
        request.status(),
        request.idAfter(),
        cursor,
        request.sortField(),
        request.sortDirection(),
        pageable
    );
  }
}
