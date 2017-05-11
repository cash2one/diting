package com.diting.dao;

import com.diting.model.Employee;
import org.apache.ibatis.annotations.Param;

/**
 * EmployeeMapper
 */
public interface EmployeeMapper {
    void create(Employee employee);

    void update(Employee employee);

    Employee get(@Param("employeeId") Integer employeeId);

    Employee checkUsernameLogin(@Param("userName")String userName, @Param("password")String password);

}
