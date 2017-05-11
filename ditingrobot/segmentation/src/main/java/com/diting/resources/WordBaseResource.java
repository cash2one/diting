package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.WordBase;
import com.diting.model.options.WordBaseOptions;
import com.diting.model.result.Results;
import com.diting.service.WordBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static com.diting.util.Utils.buildPageableOptions;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * Created by Administrator on 2016/12/30.
 */
@Path("/wordbase")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class WordBaseResource {

    @Autowired
    private WordBaseService wordBaseService;

    @POST
    @Timed
    @Path("/create")
    public Response create(WordBase wordBase){
        wordBaseService.create(wordBase);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/update")
    public Response update(WordBase wordBase){
        wordBaseService.update(wordBase);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Integer id){
        wordBaseService.delete(id);
        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/search-page")
    public Results<WordBase> searchForPage(@Context UriInfo uriInfo){
        return wordBaseService.searchForPage(buildWordBaseOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/{id}")
    public WordBase get(@PathParam("id") Integer id){
        return wordBaseService.get(id);
    }

    @PUT
    @Timed
    @Path("/batchdelete")
    public Response adminBatchDelete(@Context UriInfo uriInfo) {
        WordBaseOptions options= buildWordBaseOptions(uriInfo);
        String[] strings = options.getIds().split(",");
        wordBaseService.adminBatchDelete(strings);
        return Response.status(200).build();
    }

    private WordBaseOptions buildWordBaseOptions(@Context UriInfo uriInfo){
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        WordBaseOptions options = new WordBaseOptions();
        buildPageableOptions(uriInfo, options);
        options.setKeyword(nullIfEmpty(params.getFirst("keyword")));
        options.setType(nullIfEmpty(params.getFirst("type")));
        return options;
    }
}
