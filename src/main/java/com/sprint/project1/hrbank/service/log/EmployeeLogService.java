package com.sprint.project1.hrbank.service.log;

import com.sprint.project1.hrbank.entity.employee.Employee;

public interface EmployeeLogService {
  public void createLog(Employee before, Employee after, String memo, String ipAddress);


}
