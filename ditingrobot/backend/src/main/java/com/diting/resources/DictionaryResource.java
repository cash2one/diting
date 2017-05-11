package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.dict.Dictionary;
import com.diting.model.options.DictionaryOptions;
import com.diting.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static com.diting.util.Utils.nullIfEmpty;

/**
 * DictionaryResource
 */
@Path("/dictionaries")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class DictionaryResource {

    @Autowired
    private DictionaryService dictionaryService;

    @POST
    @Timed
    @Path("/")
    public Dictionary create(Dictionary dictionary) {
        return dictionaryService.create(dictionary);
    }

    @PUT
    @Timed
    @Path("/update")
    public Dictionary update(Dictionary dictionary) {
        return dictionaryService.update(dictionary);
    }

    @GET
    @Timed
    @Path("/{dictId}")
    public Dictionary get(@PathParam("dictId") Integer dictId) {
        return dictionaryService.get(dictId);
    }

    @GET
    @Timed
    @Path("/search-all")
    public List<Dictionary> searchAll(@Context UriInfo uriInfo) {
        return dictionaryService.search(buildAccountOptions(uriInfo));
    }

    private DictionaryOptions buildAccountOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        DictionaryOptions options = new DictionaryOptions();
        options.setTag(nullIfEmpty(params.getFirst("tag")));
        return options;
    }


}
