package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Agent;
import com.diting.model.options.AgentOptions;
import com.diting.model.result.Results;
import com.diting.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static com.diting.util.Utils.buildPageableOptions;

/**
 * AgentResource
 */
@Path("/agents")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class AgentResource {

    @Autowired
    private AgentService agentService;

    @POST
    @Timed
    @Path("/")
    public Agent create(Agent agent) {
        return agentService.create(agent);
    }

    @POST
    @Timed
    @Path("/register")
    public Agent register(Agent agent) {
        return agentService.register(agent);
    }

    @PUT
    @Timed
    @Path("/update")
    public Agent update(Agent account) {
        return agentService.update(account);
    }

    @GET
    @Timed
    @Path("/{agentId}")
    public Agent getAccountById(@PathParam("agentId") Integer agentId) {
        return agentService.get(agentId);
    }

    @POST
    @Timed
    @Path("/login")
    public Agent login(Agent agent) {
        return agentService.login(agent);
    }

    @GET
    @Timed
    @Path("/check-mobile/{mobile}")
    public Response checkMobileExists(@PathParam("mobile") String mobile) {
        return agentService.checkMobileExists(mobile) ? Response.status(400).build() : Response.ok().build();
    }

    @GET
    @Timed
    @Path("/check-user-name/{userName}")
    public Response checkUsernameExists(@PathParam("userName") String userName) {
        return agentService.checkUsernameExists(userName) ? Response.status(400).build() : Response.ok().build();
    }

    @GET
    @Timed
    @Path("/search-page")
    public Results<Agent> searchForPage(@Context UriInfo uriInfo) {
        return agentService.searchForPage(buildAccountOptions(uriInfo));
    }

    private AgentOptions buildAccountOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        AgentOptions options = new AgentOptions();
        buildPageableOptions(uriInfo, options);
        options.setKeywords(params.getFirst("keywords"));
        return options;
    }


}
