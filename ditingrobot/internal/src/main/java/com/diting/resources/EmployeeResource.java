package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Account;
import com.diting.model.Employee;
import com.diting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * EmployeeResource
 */
@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class EmployeeResource {

    @Autowired
    private EmployeeService employeeService;

    @POST
    @Timed
    @Path("/")
    public Employee create(Employee employee) {
        return employeeService.create(employee);
    }

    @PUT
    @Timed
    @Path("/update")
    public Employee update(Employee employee) {
        return employeeService.update(employee);
    }

    @GET
    @Timed
    @Path("/{employeeId}")
    public Employee get(@PathParam("employeeId") Integer employeeId) {
        return employeeService.get(employeeId);
    }

    @POST
    @Timed
    @Path("/login")
    public Employee login(Employee employee) {
        return employeeService.login(employee);
    }
}
