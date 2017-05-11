package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Home;
import com.diting.service.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Administrator on 2017/4/12.
 */
@Path("/home")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class HomePageResource {

    @Autowired
    private HomePageService homePageService;

    @GET
    @Timed
    @Path("/yesterday")
    public Home searchForPage() {
        return homePageService.findHome();
    }

    @GET
    @Timed
    @Path("/yesterday/new-knowledge-num")
    public Home searchYesterdayNewKnowledgeNum() {
        return homePageService.findYesterdayNewKnowledgeNum();
    }
}
