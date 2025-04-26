package com.sprint.project1.hrbank.controller.department;

import com.sprint.project1.hrbank.dto.department.DepartmentCreateRequest;
import com.sprint.project1.hrbank.dto.department.DepartmentPageResponse;
import com.sprint.project1.hrbank.dto.department.DepartmentResponse;
import com.sprint.project1.hrbank.dto.department.DepartmentSearchRequest;
import com.sprint.project1.hrbank.dto.department.DepartmentUpdateRequest;
import com.sprint.project1.hrbank.service.department.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(@Validated @RequestBody DepartmentCreateRequest request) {
        DepartmentResponse response = departmentService.createDepartment(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponse> getDepartment(@PathVariable Long departmentId) {
        DepartmentResponse response = departmentService.getDepartment(departmentId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<DepartmentPageResponse> getDepartmentPage(
        @ModelAttribute DepartmentSearchRequest request) {
        DepartmentPageResponse response = departmentService.getDepartmentPage(request);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponse> updateDepartment(@PathVariable Long departmentId, @Validated @RequestBody DepartmentUpdateRequest request) {
        DepartmentResponse response = departmentService.updateDepartment(departmentId, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long departmentId) {
        departmentService.deleteDepartment(departmentId);

        return ResponseEntity.noContent().build();
    }
}

