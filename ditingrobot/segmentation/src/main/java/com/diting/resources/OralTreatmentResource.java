package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.OralTreatment;
import com.diting.model.options.OralTreatmentOptions;
import com.diting.model.result.Results;
import com.diting.service.OralTreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static com.diting.util.Utils.buildPageableOptions;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * Created by Administrator on 2017/1/3.
 */
@Path("/oraltreatment")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class OralTreatmentResource {
    @Autowired
    private OralTreatmentService oralTreatmentService;

    @POST
    @Timed
    @Path("/create")
    public Response create(OralTreatment oralTreatment){
        oralTreatmentService.create(oralTreatment);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/update")
    public Response update(OralTreatment oralTreatment){
        oralTreatmentService.update(oralTreatment);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Integer id){
        oralTreatmentService.delete(id);
        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/search-page")
    public Results<OralTreatment> searchForPage(@Context UriInfo uriInfo){
        return oralTreatmentService.searchForPage(buildOralTreatmentOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/{id}")
    public OralTreatment get(@PathParam("id") Integer id) {
        return oralTreatmentService.get(id);
    }

    private OralTreatmentOptions buildOralTreatmentOptions(@Context UriInfo uriInfo){
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        OralTreatmentOptions options = new OralTreatmentOptions();
        buildPageableOptions(uriInfo, options);
        options.setKeyword(nullIfEmpty(params.getFirst("keyword")));
        return options;
    }
}
