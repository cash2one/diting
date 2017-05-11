package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Synonym;
import com.diting.model.options.SynonymOptions;
import com.diting.model.result.Results;
import com.diting.service.SynonymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static com.diting.util.Utils.buildPageableOptions;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * Created by Administrator on 2017/1/3.
 */
@Path("/synonym")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class SynonymResources {

    @Autowired
    private SynonymService synonymService;
    @POST
    @Timed
    @Path("/create")
    public Response create(Synonym synonym){
        synonymService.create(synonym);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/update")
    public Response update(Synonym synonym){
        synonymService.update(synonym);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Integer id){
        synonymService.delete(id);
        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/search-page")
    public Results<Synonym> searchForPage(@Context UriInfo uriInfo){
        return synonymService.searchForPage(buildSynonymOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/{id}")
    public Synonym get(@PathParam("id") Integer id){
        return synonymService.get(id);
    }

    private SynonymOptions buildSynonymOptions(@Context UriInfo uriInfo){
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        SynonymOptions options = new SynonymOptions();
        buildPageableOptions(uriInfo, options);
        options.setKeyword(nullIfEmpty(params.getFirst("keyword")));
        return options;
    }
}
