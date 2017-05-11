package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.core.Universe;
import com.diting.model.CustomerSynonym;
import com.diting.model.options.CustomerSynonymOptions;
import com.diting.model.result.Results;
import com.diting.service.CustomerSynonymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static com.diting.util.Utils.buildPageableOptions;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * Created by liufei on 2016/9/8.
 */

@Path("/customersynonym")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class CustomerSynonymResource {

    @Autowired
    CustomerSynonymService customerSynonymService;

    @POST
    @Timed
    @Path("/create")
    public CustomerSynonym customerSynonymCreate(CustomerSynonym customerSynonym) {
        return customerSynonymService.create(customerSynonym);
    }

    @GET
    @Timed
    @Path("/search-page")
    public Results<CustomerSynonym> searchForPage(@Context UriInfo uriInfo) {
        return customerSynonymService.searchForPage(buildKnowledgeOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/{customerSynonymId}")
    public CustomerSynonym get(@PathParam("customerSynonymId") Integer customerSynonymId) {
        return customerSynonymService.get(customerSynonymId);
    }

    @POST
    @Timed
    @Path("/delete/{customerSynonymId}")
    public Response delete(@PathParam("customerSynonymId") Integer customerSynonymId) {
        customerSynonymService.delete(customerSynonymId);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/update")
    public CustomerSynonym customerSynonymUpdate(CustomerSynonym customerSynonym) {
        return customerSynonymService.update(customerSynonym);
    }
    private CustomerSynonymOptions buildKnowledgeOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        CustomerSynonymOptions options = new CustomerSynonymOptions();
        buildPageableOptions(uriInfo, options);
        options.setAccountId(Universe.current().getUserId());
        if (nullIfEmpty(params.getFirst("keywords"))!=null){
            options.setKeywords(params.getFirst("keywords"));
        }

        return options;
    }
}
