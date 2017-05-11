package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.RegistrationUser;
import com.diting.service.RegistrationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
@Path("/registration")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class RegistrationUserResource {

    @Autowired
    private RegistrationUserService registrationUserService;

    @POST
    @Timed
    @Path("/create")
    public RegistrationUser create(RegistrationUser registrationUser){
        return registrationUserService.create(registrationUser);
    }

    @GET
    @Timed
    @Path("/search")
    public List<RegistrationUser> search(){
        return registrationUserService.search();
    }

    @GET
    @Timed
    @Path("/search/vote")
    public List<RegistrationUser> searchForVote() {
        return registrationUserService.searchForVote();
    }

    @GET
    @Timed
    @Path("/check-mobile/{mobile}")
    public Response checkMobile(@PathParam("mobile") String mobile) {
        return registrationUserService.checkMobileExists(mobile) ? Response.status(400).build() : Response.ok().build();
    }

    @GET
    @Timed
    @Path("/vote/{mobile}")
    public Response vote(@PathParam("mobile") String mobile) {
        registrationUserService.vote(mobile);
        return Response.status(200).build();
    }

}
