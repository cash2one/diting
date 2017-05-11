package com.diting.service;

import com.diting.model.Employee;

/**
 * EmployeeService.
 */
public interface EmployeeService {

    Employee create(Employee employee);

    Employee update(Employee employee);

    Employee get(Integer employeeId);

    Employee login(Employee employee);
}
