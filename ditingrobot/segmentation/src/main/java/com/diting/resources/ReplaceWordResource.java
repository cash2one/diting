package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.ReplaceWord;
import com.diting.model.options.ReplaceWordOptions;
import com.diting.model.result.Results;
import com.diting.service.ReplaceWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static com.diting.util.Utils.buildPageableOptions;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * Created by Administrator on 2017/1/3.
 */
@Path("/replaceword")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ReplaceWordResource {

    @Autowired
    private ReplaceWordService replaceWordService;

    @POST
    @Timed
    @Path("/create")
    public Response create(ReplaceWord replaceWord){
        replaceWordService.create(replaceWord);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/update")
    public Response update(ReplaceWord replaceWord){
        replaceWordService.update(replaceWord);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Integer id){
        replaceWordService.delete(id);
        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/search-page")
    public Results<ReplaceWord> searchForPage(@Context UriInfo uriInfo){
        return replaceWordService.searchForPage(buildReplaceWordOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/{id}")
    public ReplaceWord get(@PathParam("id") Integer id){
        return replaceWordService.get(id);
    }

    @PUT
    @Timed
    @Path("/batchdelete")
    public Response adminBatchDelete(@Context UriInfo uriInfo) {
        ReplaceWordOptions options= buildReplaceWordOptions(uriInfo);
        String[] strings = options.getIds().split(",");
        replaceWordService.adminBatchDelete(strings);
        return Response.status(200).build();
    }

    private ReplaceWordOptions buildReplaceWordOptions(@Context UriInfo uriInfo){
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        ReplaceWordOptions options = new ReplaceWordOptions();
        buildPageableOptions(uriInfo, options);
        options.setKeyword(nullIfEmpty(params.getFirst("keyword")));
        options.setIds(nullIfEmpty(params.getFirst("ids")));
        return options;
    }
}
