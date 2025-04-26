package com.sprint.project1.hrbank.service.department;

import com.sprint.project1.hrbank.dto.department.DepartmentCreateRequest;
import com.sprint.project1.hrbank.dto.department.DepartmentPageResponse;
import com.sprint.project1.hrbank.dto.department.DepartmentResponse;
import com.sprint.project1.hrbank.dto.department.DepartmentSearchRequest;
import com.sprint.project1.hrbank.dto.department.DepartmentUpdateRequest;
import com.sprint.project1.hrbank.entity.department.Department;
import com.sprint.project1.hrbank.exception.department.DepartmentDeletionNotAllowedException;
import com.sprint.project1.hrbank.exception.department.DepartmentNotFoundException;
import com.sprint.project1.hrbank.exception.util.InvalidCursorFormatException;
import com.sprint.project1.hrbank.mapper.department.DepartmentMapper;
import com.sprint.project1.hrbank.repository.department.DepartmentRepository;
import com.sprint.project1.hrbank.repository.employee.EmployeeRepository;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            .orElseThrow(() -> new DepartmentNotFoundException("부서를 찾을 수 없습니다."));

        long employeeCount = getEmployeeCountBy(department);
        return departmentMapper.toResponse(department, employeeCount);
    }


    @Override
    public DepartmentPageResponse getDepartmentPage(DepartmentSearchRequest request) {
        String keyword = extractKeywordFromRequest(request);
        Pageable pageable = PageRequest.of(0, request.size() + 1);
        String sortField = request.sortField();
        String sortDirection = request.sortDirection();

        String cursor = (request.cursor() != null) ? decodeCursor(request.cursor()) : null;
        LocalDate cursorDate = ("establishedDate".equals(sortField) && cursor != null)
            ? parseCursorDate(cursor)
            : null;

        List<Department> departments = getDepartments(cursor, sortField, sortDirection,
            keyword, pageable, cursorDate);

        boolean hasNext = departments.size() == request.size() + 1;
        Department nextCursorDepartment = getNextCursorDepartment(hasNext, departments);
        Long nextIdAfter = getNextIdAfter(hasNext, nextCursorDepartment);
        String nextCursor = getNextCursor(hasNext, nextCursorDepartment, sortField);

        Long totalElements = departmentRepository.countByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);

        List<Long> departmentIds = departments.stream()
            .map(Department::getId)
            .toList();

        Map<Long, Long> employeeCountPerDepartment = departmentRepository.countEmployeesByDepartmentIds(departmentIds)
            .stream()
            .collect(Collectors.toMap(
                row -> (Long) row[0],
                row -> (Long) row[1]
            ));

        List<DepartmentResponse> content = departments.stream()
            .map(dept -> departmentMapper.toResponse(dept, employeeCountPerDepartment.getOrDefault(dept.getId(), 0L)))
            .toList();

        Integer size = (departments.size() - 1);
        return new DepartmentPageResponse(
            content,
            nextCursor,
            nextIdAfter,
            size,
            hasNext,
            totalElements
        );
    }

    @Override
    public DepartmentResponse updateDepartment(Long departmentId, DepartmentUpdateRequest updateRequest) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("부서를 찾을 수 없습니다."));
        Department other = departmentRepository.findByName(updateRequest.name());
        department.validateNotDuplicateWith(other);

        long employeeCount = getEmployeeCountBy(department);

        department.update(updateRequest.name(), updateRequest.establishedDate(), updateRequest.description());
        return departmentMapper.toResponse(department, employeeCount);
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("부서를 찾을 수 없습니다."));

        long employeeCount = getEmployeeCountBy(department);
        if (employeeCount > 0) {
            throw new DepartmentDeletionNotAllowedException("부서를 삭제할 수 없습니다.");
        }
        departmentRepository.delete(department);
    }

    private long getEmployeeCountBy(Department department) {
        long employeeCount = employeeRepository.countByDepartmentId(department.getId());
        return employeeCount;
    }

    private static Long getNextIdAfter(boolean hasNext, Department nextCursorDepartment) {
        return hasNext
            ? nextCursorDepartment.getId()
            : null;
    }

    private static Department getNextCursorDepartment(boolean hasNext,
        List<Department> departments) {
        return hasNext
            ? departments.get(departments.size() - 2)
            : null;
    }

    private List<Department> getDepartments(String cursor, String sortField, String sortDirection,
        String keyword, Pageable pageable, LocalDate cursorDate) {
        List<Department> departments;
        if (cursor == null) {
            if (sortField.equals("name")) {
                departments = sortDirection.equals("desc") ?
                    departmentRepository.findFirstPageByNameDesc(keyword, pageable)
                    : departmentRepository.findFirstPageByNameAsc(keyword, pageable);
            } else {
                departments = sortDirection.equals("desc") ?
                    departmentRepository.findFirstPageByEstablishedDateDesc(keyword, pageable)
                    : departmentRepository.findFirstPageByEstablishedDateAsc(keyword, pageable);
            }
        } else {
            if (sortField.equals("name")) {
                departments = sortDirection.equals("desc") ?
                    departmentRepository.findNextPageByNameDesc(keyword, cursor, pageable)
                    : departmentRepository.findNextPageByNameAsc(keyword, cursor, pageable);
            } else {
                departments = sortDirection.equals("desc") ?
                    departmentRepository.findNextPageByEstablishedDateDesc(keyword, cursorDate,
                        pageable)
                    : departmentRepository.findNextPageByEstablishedDateAsc(keyword, cursorDate,
                        pageable);
            }
        }
        return departments;
    }

    private static LocalDate parseCursorDate(String cursor) {
        try {
            return LocalDate.parse(cursor, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new InvalidCursorFormatException("잘못된 커서 형식입니다.");
        }
    }


    private static String getNextCursor(Department cursorDepartment, String sortField) {
        if (sortField.equals("name")) {
            return encodeCursor(cursorDepartment.getName());
        }
        return encodeCursor(cursorDepartment.getEstablishedDate().toString());
    }

    private static String extractKeywordFromRequest(DepartmentSearchRequest request) {
        return (request.nameOrDescription() != null && !request.nameOrDescription().isBlank())
            ? request.nameOrDescription().toLowerCase() : "";
    }

    public static String encodeCursor(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }


    private static String decodeCursor(String cursor) {
        try {
            return new String(Base64.getDecoder().decode(cursor.toString()), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new InvalidCursorFormatException("잘못된 커서 형식입니다.");
        }
    }

    private static String getNextCursor(boolean hasNext, Department nextCursorDepartment,
        String sortField) {
        return hasNext
            ? getNextCursor(nextCursorDepartment, sortField)
            : null;
    }
}
