package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.options.ExternalOptions;
import com.diting.service.ExternalApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * ExternalApplicationResource
 */
@Path("/ex-applications")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ExternalApplicationResource {

    @Autowired
    private ExternalApplicationService externalApplicationService;

    @POST
    @Timed
    @Path("/setting")
    public Response create(ExternalOptions externalOptions) {
        externalApplicationService.setting(externalOptions);
        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/search-checked")
    public List<Integer> searchForChecked() {
        return externalApplicationService.searchForChecked();
    }

}
