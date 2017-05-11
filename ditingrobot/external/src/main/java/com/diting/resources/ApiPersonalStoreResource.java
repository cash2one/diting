package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.ApiPersonalStore;
import com.diting.model.options.ApiPersonalStoreOptions;
import com.diting.model.result.Results;
import com.diting.resources.request.ApiSaveRequest;
import com.diting.service.ApiPersonalStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.List;

import static com.diting.util.Utils.buildPageableOptions;

/**
 * Created by Administrator on 2017/3/7.
 */
@Path("/apis/personal")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ApiPersonalStoreResource {
    @Autowired
    private ApiPersonalStoreService apiPersonalStoreService;

    @POST
    @Timed
    @Path("/save")
    public ApiPersonalStore save(ApiSaveRequest apiPersonalStore) {
        return apiPersonalStoreService.create(apiPersonalStore.getApiPersonalStore());
    }

    @PUT
    @Timed
    @Path("/update")
    public ApiPersonalStore update(ApiPersonalStore apiPersonalStore) {
        return apiPersonalStoreService.update(apiPersonalStore);
    }

    @GET
    @Timed
    @Path("/{apiPersonalStoreId}")
    public ApiPersonalStore get(@PathParam("apiPersonalStoreId") Integer apiPersonalStoreId) {
        return apiPersonalStoreService.get(apiPersonalStoreId);
    }

    @GET
    @Timed
    @Path("/find_personal_store")
    public List<ApiPersonalStore> searchPersonalStore(@Context UriInfo uriInfo) {
        return apiPersonalStoreService.searchPersonal();
    }

    @GET
    @Timed
    @Path("/search_personal_forpage")
    public Results<ApiPersonalStore> searchPersonalForPage(@Context UriInfo uriInfo) {
        return apiPersonalStoreService.searchPersonalForPage(buildOptions(uriInfo));
    }

    @PUT
    @Timed
    @Path("/batchdelete")
    public Response batchDelete(ApiPersonalStore apiPersonalStore) {
        String[] strings = apiPersonalStore.getIds().split(",");
        apiPersonalStoreService.batchDelete(strings);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/batchsubmit")
    public Response batchSubmit(ApiPersonalStore apiPersonalStore) {
        String[] strings = apiPersonalStore.getIds().split(",");
        apiPersonalStoreService.batchSubmit(strings);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/{apiPersonalStoreId}/switch/{status}/update")
    public Response switchUpdate(@PathParam("apiPersonalStoreId") Integer apiStoreId, @PathParam("status") Integer status) {
        apiPersonalStoreService.switchUpdate(apiStoreId, status);
        return Response.status(200).build();
    }
    private ApiPersonalStoreOptions buildOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        ApiPersonalStoreOptions options = new ApiPersonalStoreOptions();
        buildPageableOptions(uriInfo, options);
        return options;
    }
}
