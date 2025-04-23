package com.sprint.project1.hrbank.controller.department;

import com.sprint.project1.hrbank.dto.department.DepartmentCreateRequest;
import com.sprint.project1.hrbank.dto.department.DepartmentResponse;
import com.sprint.project1.hrbank.dto.department.DepartmentUpdateRequest;
import com.sprint.project1.hrbank.service.department.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody DepartmentCreateRequest request) {
        DepartmentResponse response = departmentService.createDepartment(request);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponse> updateDepartment(@PathVariable Long departmentId, @RequestBody DepartmentUpdateRequest request) {
        DepartmentResponse response = departmentService.updateDepartment(departmentId, request);
        return ResponseEntity.ok().body(response);
    }
}

