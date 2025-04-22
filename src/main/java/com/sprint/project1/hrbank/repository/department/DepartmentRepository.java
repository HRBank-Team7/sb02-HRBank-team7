package com.sprint.project1.hrbank.repository.department;

import com.sprint.project1.hrbank.entity.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByName(String name);
}
