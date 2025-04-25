package com.sprint.project1.hrbank.service.department;

import com.sprint.project1.hrbank.dto.department.DepartmentCreateRequest;
import com.sprint.project1.hrbank.dto.department.DepartmentPageResponse;
import com.sprint.project1.hrbank.dto.department.DepartmentResponse;
import com.sprint.project1.hrbank.dto.department.DepartmentSearchRequest;
import com.sprint.project1.hrbank.dto.department.DepartmentUpdateRequest;

public interface DepartmentService {
    DepartmentResponse createDepartment(DepartmentCreateRequest createRequest);
    DepartmentResponse getDepartment(Long departmentId);
    DepartmentPageResponse getDepartmentPage(DepartmentSearchRequest request);
    DepartmentResponse updateDepartment(Long departmentId, DepartmentUpdateRequest updateRequest);
    void deleteDepartment(Long departmentId);
}
