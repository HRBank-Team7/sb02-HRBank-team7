package com.sprint.project1.hrbank.mapper.department;

import com.sprint.project1.hrbank.dto.department.DepartmentDto;
import com.sprint.project1.hrbank.entity.department.Department;
import org.springframework.stereotype.Component;
import java.time.ZoneId;

@Component
public class DepartmentMapper {

    public DepartmentDto toDto(Department department, int employeeCount) {
        return new DepartmentDto(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.getEstablishedDate().atZone(ZoneId.of("Asia/Seoul")).toLocalDate(),
                employeeCount
        );
    }
}
