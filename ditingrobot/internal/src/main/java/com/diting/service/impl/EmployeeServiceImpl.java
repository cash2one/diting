package com.diting.service.impl;

import com.diting.dao.EmployeeMapper;
import com.diting.error.AppErrors;
import com.diting.model.Account;
import com.diting.model.Employee;
import com.diting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.diting.util.Utils.sha1;

@Service("employeeService")
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee create(Employee employee){
        employee.setPassword(sha1(employee.getUserName(), employee.getPassword()));
        employeeMapper.create(employee);
        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        employeeMapper.update(employee);
        return employee;
    }

    @Override
    public Employee get(Integer employeeId) {
        return employeeMapper.get(employeeId);
    }

    @Override
    public Employee login(Employee employee){
        // check username not null
        if (employee.getUserName() == null)
            throw AppErrors.INSTANCE.missingField("username").exception();

        // check password not null
        if (employee.getPassword() == null)
            throw AppErrors.INSTANCE.missingField("password").exception();

        // try to login account
        Employee result = employeeMapper.checkUsernameLogin(employee.getUserName(), sha1(employee.getUserName(), employee.getPassword()));

        if (result == null)
            throw AppErrors.INSTANCE.loginFailed().exception();

        return result;
    }

}
