package com.sprint.project1.hrbank.service.department;

import com.sprint.project1.hrbank.dto.department.DepartmentCreateRequest;
import com.sprint.project1.hrbank.dto.department.DepartmentResponse;
import com.sprint.project1.hrbank.dto.department.DepartmentUpdateRequest;
import com.sprint.project1.hrbank.entity.department.Department;
import com.sprint.project1.hrbank.mapper.department.DepartmentMapper;
import com.sprint.project1.hrbank.repository.department.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponse createDepartment(DepartmentCreateRequest createRequest) {
        Department department = new Department(createRequest.name(), createRequest.establishedDate(), createRequest.description());
        Department other = departmentRepository.findByName(createRequest.name());
        department.validateNotDuplicateWith(other);

        departmentRepository.save(department);
        return departmentMapper.toResponse(department, 10);
    }

    @Override
    public DepartmentResponse updateDepartment(Long departmentId, DepartmentUpdateRequest updateRequest) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new NoSuchElementException("department not found" + departmentId));
        Department other = departmentRepository.findByName(updateRequest.name());
        department.validateNotDuplicateWith(other);

        department.update(updateRequest.name(), updateRequest.establishedDate(), updateRequest.description());
        return departmentMapper.toResponse(department, 10);
    }
}
