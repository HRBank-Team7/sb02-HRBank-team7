package com.sprint.project1.hrbank.service.department;

import com.sprint.project1.hrbank.dto.department.DepartmentCreateRequest;
import com.sprint.project1.hrbank.dto.department.DepartmentResponse;
import com.sprint.project1.hrbank.dto.department.DepartmentUpdateRequest;
import com.sprint.project1.hrbank.entity.department.Department;
import com.sprint.project1.hrbank.mapper.department.DepartmentMapper;
import com.sprint.project1.hrbank.repository.department.DepartmentRepository;
import com.sprint.project1.hrbank.repository.employee.EmployeeRepository;
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
    private final EmployeeRepository employeeRepository;

    @Override
    public DepartmentResponse createDepartment(DepartmentCreateRequest createRequest) {
        Department department = new Department(createRequest.name(), createRequest.establishedDate(), createRequest.description());
        Department other = departmentRepository.findByName(createRequest.name());
        department.validateNotDuplicateWith(other);

        departmentRepository.save(department);
        long employeeCount = getEmployeeCountBy(department);

        return departmentMapper.toResponse(department, employeeCount);
    }


    @Override
    public DepartmentResponse getDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
            .orElseThrow(() -> new NoSuchElementException("department not found" + departmentId));

        long employeeCount = getEmployeeCountBy(department);
        return departmentMapper.toResponse(department, employeeCount);
    }

    @Override
    public DepartmentResponse updateDepartment(Long departmentId, DepartmentUpdateRequest updateRequest) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new NoSuchElementException("department not found" + departmentId));
        Department other = departmentRepository.findByName(updateRequest.name());
        department.validateNotDuplicateWith(other);

        long employeeCount = getEmployeeCountBy(department);

        department.update(updateRequest.name(), updateRequest.establishedDate(), updateRequest.description());
        return departmentMapper.toResponse(department, employeeCount);
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new NoSuchElementException("department not found" + departmentId));

        long employeeCount = getEmployeeCountBy(department);
        if (employeeCount > 0) {
            throw new IllegalStateException("소속 직원이 있는 부서는 삭제할 수 없습니다.");
        }
        departmentRepository.delete(department);
    }
    
    private long getEmployeeCountBy(Department department) {
        long employeeCount = employeeRepository.countByDepartmentId(department.getId());
        return employeeCount;
    }
}
