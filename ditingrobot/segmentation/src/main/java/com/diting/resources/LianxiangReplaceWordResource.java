package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.LianxiangReplaceWord;
import com.diting.model.options.LianxiangReplaceWordOptions;
import com.diting.model.result.Results;
import com.diting.service.LianxiangReplaceWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static com.diting.util.Utils.buildPageableOptions;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * Created by Administrator on 2017/1/3.
 */
@Path("/lianxiangreplaceword")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class LianxiangReplaceWordResource {
    @Autowired
    private LianxiangReplaceWordService lianxiangReplaceWordService;

    @POST
    @Timed
    @Path("/create")
    public Response create(LianxiangReplaceWord lianxiangReplaceWord) {
        lianxiangReplaceWordService.create(lianxiangReplaceWord);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/update")
    public Response update(LianxiangReplaceWord lianxiangReplaceWord) {
        lianxiangReplaceWordService.update(lianxiangReplaceWord);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Integer id) {
        lianxiangReplaceWordService.delete(id);
        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/search-page")
    public Results<LianxiangReplaceWord> searchForPage(@Context UriInfo uriInfo) {
        return lianxiangReplaceWordService.searchForPage(buildLianxiangReplaceWordOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/{id}")
    public LianxiangReplaceWord get(@PathParam("id") Integer id) {
        return lianxiangReplaceWordService.get(id);
    }

    private LianxiangReplaceWordOptions buildLianxiangReplaceWordOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        LianxiangReplaceWordOptions options = new LianxiangReplaceWordOptions();
        buildPageableOptions(uriInfo, options);
        options.setKeyword(nullIfEmpty(params.getFirst("keyword")));
        return options;
    }
}
