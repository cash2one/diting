package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.views.AppInfo;
import com.diting.service.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Administrator on 2017/2/16.
 */
@SuppressWarnings("ALL")
@Path("/app/info")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class AppInfoResource {

    @Autowired
    private AppInfoService appInfoService;

    @GET
    @Timed
    @Path("/search")
    public AppInfo getAppinfo(){
        return appInfoService.getAppInfo();
    }
}
