package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.StaAccount;
import com.diting.model.options.PageableOptions;
import com.diting.model.result.Results;
import com.diting.service.StaAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

import static com.diting.util.Utils.*;

/**
 * StaAccountResource
 */
@Path("/sta-accounts")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class StaAccountResource {

    @Autowired
    private StaAccountService staAccountService;

    @POST
    @Timed
    @Path("/")
    public StaAccount create(StaAccount staAccount) {
        return staAccountService.create(staAccount);
    }

    @PUT
    @Timed
    @Path("/update")
    public StaAccount updateStaAccount(StaAccount staAccount) {
        return staAccountService.update(staAccount);
    }

    @GET
    @Timed
    @Path("/search-page")
    public Results<StaAccount> searchForPage(@Context UriInfo uriInfo) {
        PageableOptions pageableOptions=new PageableOptions();
        buildPageableOptions(uriInfo, pageableOptions);
        return staAccountService.searchForPage(pageableOptions);
    }

    @GET
    @Timed
    @Path("/export-sta-account")
    public List<StaAccount> companyKnowledgeExcelList() {
        return staAccountService.findAllStaAccount();
    }
}
