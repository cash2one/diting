package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Thesaurus;
import com.diting.model.options.ThesaurusOptions;
import com.diting.model.result.Results;
import com.diting.service.ThesaurusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static com.diting.util.Utils.buildPageableOptions;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * Created by Administrator on 2017/1/3.
 */
@Path("/thesaurus")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ThesaurusResource {
    @Autowired
    private ThesaurusService thesaurusService;

    @POST
    @Timed
    @Path("/create")
    public Response create(Thesaurus thesaurus){
        thesaurusService.create(thesaurus);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/update")
    public Response update(Thesaurus thesaurus){
        thesaurusService.update(thesaurus);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Integer id){
        thesaurusService.delete(id);
        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/search-page")
    public Results<Thesaurus> searchForPage(@Context UriInfo uriInfo){
        return thesaurusService.searchForPage(buildThesaurusOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/{id}")
    public Thesaurus get(@PathParam("id") Integer id){
        return thesaurusService.get(id);
    }

    private ThesaurusOptions buildThesaurusOptions(@Context UriInfo uriInfo){
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        ThesaurusOptions options = new ThesaurusOptions();
        buildPageableOptions(uriInfo, options);
        options.setKeyword(nullIfEmpty(params.getFirst("keyword")));
        return options;
    }
}
