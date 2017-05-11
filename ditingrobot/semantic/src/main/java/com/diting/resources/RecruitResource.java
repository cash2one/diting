package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Recruit;
import com.diting.service.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Administrator on 2017/1/4.
 */
@Path("/recruit")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class RecruitResource {
    @Autowired
    private RecruitService recruitService;

    @POST
    @Path("/create")
    public Recruit create(Recruit recruit){
        recruitService.create(recruit);
        return recruit;
    }

    @POST
    @Timed
    @Path("/update")
    public Recruit update(Recruit recruit){
        return recruitService.update(recruit);
    }

    @GET
    @Timed
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Integer id){
        recruitService.delete(id);
        return Response.status(200).build();
    }
}
