package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Robot;
import com.diting.service.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * RobotResource
 */
@Path("/robots")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class RobotResource {

    @Autowired
    private RobotService robotService;

    @POST
    @Timed
    @Path("/")
    public Robot create(Robot robot) {
        return robotService.create(robot);
    }

    @POST
    @Timed
    @Path("/save")
    public Robot save(Robot robot) {
        return robotService.save(robot);
    }

    @PUT
    @Timed
    @Path("/update")
    public Robot update(Robot robot) {
        return robotService.update(robot);
    }

    @GET
    @Timed
    @Path("/{robotId}")
    public Robot get(@PathParam("robotId") Integer robotId) {
        return robotService.get(robotId);
    }

    @GET
    @Timed
    @Path("/user/{userId}")
    public Robot getByUserId(@PathParam("userId") Integer userId) {
        return robotService.getByUserId(userId);
    }

    @GET
    @Timed
    @Path("/unique_id/{unique_id}")
    public Robot getByUniqueId(@PathParam("unique_id") String unique_id) {
        return robotService.getByUniqueId(unique_id);
    }

    @GET
    @Timed
    @Path("/domain_name/{domainName}")
    public Robot getByDomainName(@PathParam("domainName") String domainName) {
        return robotService.getByDomainName(domainName);
    }

    @GET
    @Timed
    @Path("/claim-robots")
    public List<Robot> getClaimRobots() {
        return robotService.getClaimRobots();
    }


}
