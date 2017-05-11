package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.SensitiveWord;
import com.diting.model.options.SensitiveWordOptions;
import com.diting.model.result.Results;
import com.diting.service.SensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static com.diting.util.Utils.buildPageableOptions;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * Created by Administrator on 2017/1/3.
 */
@Path("/sensitiveword")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class SensitiveWordResource {
    @Autowired
    private SensitiveWordService sensitiveWordService;

    @POST
    @Timed
    @Path("/create")
    public Response create(SensitiveWord sensitiveWord) {
        sensitiveWordService.create(sensitiveWord);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/update")
    public Response update(SensitiveWord sensitiveWord) {
        sensitiveWordService.update(sensitiveWord);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Integer id) {
        sensitiveWordService.delete(id);
        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/search-page")
    public Results<SensitiveWord> searchForPage(@Context UriInfo uriInfo) {
        return sensitiveWordService.searchForPage(buildSensitiveWordOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/{id}")
    public SensitiveWord get(@PathParam("id") Integer id) {
        return sensitiveWordService.get(id);
    }

    private SensitiveWordOptions buildSensitiveWordOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        SensitiveWordOptions options = new SensitiveWordOptions();
        buildPageableOptions(uriInfo, options);
        options.setKeyword(nullIfEmpty(params.getFirst("keyword")));
        return options;
    }
}
