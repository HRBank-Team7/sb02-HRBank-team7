package com.sprint.project1.hrbank.service.department;

import com.sprint.project1.hrbank.dto.department.DepartmentCreateRequest;
import com.sprint.project1.hrbank.dto.department.DepartmentResponse;
import com.sprint.project1.hrbank.dto.department.DepartmentUpdateRequest;

public interface DepartmentService {
    DepartmentResponse createDepartment(DepartmentCreateRequest createRequest);
    DepartmentResponse updateDepartment(Long departmentId, DepartmentUpdateRequest updateRequest);
    void deleteDepartment(Long departmentId);
}
