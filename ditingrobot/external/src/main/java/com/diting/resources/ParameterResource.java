package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Parameter;
import com.diting.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * ParameterResource
 */
@Path("/parameters")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ParameterResource {

    @Autowired
    private ParameterService parameterService;

    @POST
    @Timed
    @Path("/")
    public Parameter create(Parameter parameter) {
        return parameterService.create(parameter);
    }

    @GET
    @Timed
    @Path("/{parameterId}")
    public Parameter get(@PathParam("parameterId") Integer parameterId) {
        return parameterService.get(parameterId);
    }
}
