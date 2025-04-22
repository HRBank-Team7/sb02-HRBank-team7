package com.sprint.project1.hrbank.service.department;

import com.sprint.project1.hrbank.dto.department.DepartmentCreateRequest;
import com.sprint.project1.hrbank.dto.department.DepartmentDto;

public interface DepartmentService {
    DepartmentDto createDepartment(DepartmentCreateRequest createRequest);

}
