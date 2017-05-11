package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.ApiStore;
import com.diting.model.options.ApiStoreOptions;
import com.diting.model.result.Results;
import com.diting.resources.request.ApiSaveRequest;
import com.diting.service.ApiStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.List;

import static com.diting.util.Utils.buildPageableOptions;

/**
 * ApiStoreResource
 */
@Path("/apis")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ApiStoreResource {

    @Autowired
    private ApiStoreService apiStoreService;

    @POST
    @Timed
    @Path("/")
    public ApiStore create(ApiStore apiStore) {
        return apiStoreService.create(apiStore);
    }

    @PUT
    @Timed
    @Path("/update")
    public ApiStore update(ApiStore apiStore) {
        return apiStoreService.update(apiStore);
    }

    @GET
    @Timed
    @Path("/{apiStoreId}")
    public ApiStore get(@PathParam("apiStoreId") Integer apiStoreId) {
        return apiStoreService.get(apiStoreId);
    }

    @POST
    @Timed
    @Path("/save")
    public ApiStore save(ApiSaveRequest apiSaveRequest) {
        return apiStoreService.save(apiSaveRequest);
    }

    @PUT
    @Timed
    @Path("/{apiStoreId}/switch/{status}/update")
    public Response switchUpdate(@PathParam("apiStoreId") Integer apiStoreId, @PathParam("status") Integer status) {
        apiStoreService.switchUpdate(apiStoreId, status);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/{apiStoreId}/approval/{status}/update")
    public Response approvalUpdate(@PathParam("apiStoreId") Integer apiStoreId, @PathParam("status") Integer status) {
        apiStoreService.approvalUpdate(apiStoreId, status);
        return Response.status(200).build();
    }

    @DELETE
    @Timed
    @Path("/{apiStoreId}")
    public Response delete(@PathParam("apiStoreId") Integer apiStoreId) {
        apiStoreService.delete(apiStoreId);
        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/search-page")
    public Results<ApiStore> searchForPage(@Context UriInfo uriInfo) {
        return apiStoreService.searchForPage(buildOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/search-validity")
    public List<ApiStore> searchForValidity(@Context UriInfo uriInfo) {
        return apiStoreService.searchForValidity(buildOptions(uriInfo));
    }

    private ApiStoreOptions buildOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        ApiStoreOptions options = new ApiStoreOptions();
        buildPageableOptions(uriInfo, options);
        return options;
    }

}
