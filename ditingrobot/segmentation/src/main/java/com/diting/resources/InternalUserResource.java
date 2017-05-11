package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Account;
import com.diting.model.InternalUser;
import com.diting.model.options.InternalUserOptions;
import com.diting.model.result.Results;
import com.diting.service.InternalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import static com.diting.util.Utils.buildPageableOptions;

/**
 * Created by liufei on 2016/12/28.
 */

@Path("/internal")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class InternalUserResource {

    @Autowired
    private InternalUserService internalUserService;

    @POST
    @Timed
    @Path("/create")
    public InternalUser create(InternalUser internalUser){
        internalUserService.create(internalUser);
        return internalUser;
    }

    @POST
    @Timed
    @Path("/login")
    public InternalUser login(InternalUser internalUser){
        internalUserService.login(internalUser);
        return internalUser;
    }

    @PUT
    @Timed
    @Path("/update")
    public InternalUser updateInternalUser(InternalUser internalUser) {
        return internalUserService.update(internalUser);
    }

    @GET
    @Timed
    @Path("/search-page")
    public Results<InternalUser> searchForPage(@Context UriInfo uriInfo){
        return internalUserService.searchForPage(buildAccountOptions(uriInfo));
    }

    private InternalUserOptions buildAccountOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        InternalUserOptions options = new InternalUserOptions();
        buildPageableOptions(uriInfo, options);
        return options;
    }
}
