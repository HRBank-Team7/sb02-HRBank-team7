package com.sprint.project1.hrbank.service.department;

import com.sprint.project1.hrbank.dto.department.DepartmentCreateRequest;
import com.sprint.project1.hrbank.dto.department.DepartmentDto;
import com.sprint.project1.hrbank.entity.department.Department;
import com.sprint.project1.hrbank.mapper.department.DepartmentMapper;
import com.sprint.project1.hrbank.repository.department.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentDto createDepartment(DepartmentCreateRequest createRequest) {
        Department department = new Department(createRequest.name(), createRequest.establishedDate(), createRequest.description());
        Department other = departmentRepository.findByName(createRequest.name());
        department.validateNotDuplicateWith(other);

        Department saved = departmentRepository.save(department);

        return departmentMapper.toDto(saved,10);
    }
}
