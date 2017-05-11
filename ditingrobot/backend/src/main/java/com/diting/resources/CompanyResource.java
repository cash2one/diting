package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Account;
import com.diting.model.Company;
import com.diting.model.options.AccountOptions;
import com.diting.model.options.CompanyOptions;
import com.diting.model.result.Results;
import com.diting.service.CompanyService;
import com.diting.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.List;

import static com.diting.util.Utils.buildPageableOptions;
import static com.diting.util.Utils.isEmpty;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * CompanyResource
 */
@Path("/companies")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class CompanyResource {

    @Autowired
    private CompanyService companyService;

    @POST
    @Timed
    @Path("/")
    public Company create(Company company) {
        return companyService.create(company);
    }

    @POST
    @Timed
    @Path("/save")
    public Company save(Company company) {
        return companyService.save(company);
    }

    @PUT
    @Timed
    @Path("/update")
    public Company update(Company company) {
        return companyService.update(company);
    }

    @GET
    @Timed
    @Path("/{companyId}")
    public Company get(@PathParam("companyId") Integer companyId) {
        return companyService.get(companyId);
    }

    @GET
    @Timed
    @Path("/check-name/{companyName}")
    public Response checkCompanyNameExists(@PathParam("companyName") String companyName) {
        return companyService.checkCompanyNameExists(companyName) ? Response.status(400).build() : Response.ok().build();
    }

    @GET
    @Timed
    @Path("/admin/search-page")
    public Results<Company> searchForPage(@Context UriInfo uriInfo) {
        CompanyOptions options=buildCompanyOptions(uriInfo);
        if (!isEmpty(options.getStartTime()))
            return companyService.searchForPageBytime(options);
        else
            return companyService.searchForPage(options);
    }

    @GET
    @Timed
    @Path("/egent/search-page")
    public Results<Company> searchAgentUserForPage(@Context UriInfo uriInfo) {
        return companyService.searchAgentCompanyForPage(buildCompanyOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/searchforkeyword")
    public List<Company> searchForKeyWord(@Context UriInfo uriInfo) {
        return companyService.searchForKeyWord(buildCompanyOptions(uriInfo).getKeywords());
    }

    private CompanyOptions buildCompanyOptions(@Context UriInfo uriInfo){
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();
        CompanyOptions options = new CompanyOptions();
        buildPageableOptions(uriInfo, options);
        if (nullIfEmpty(params.getFirst("keyword")) != null) {
            options.setKeywords(params.getFirst("keyword"));
        }
        if (nullIfEmpty(params.getFirst("username")) != null) {
            options.setUsername(params.getFirst("username"));
        }
        if (nullIfEmpty(params.getFirst("invitationCode")) != null) {
            options.setInvitationCode(params.getFirst("invitationCode"));
        }
        if (nullIfEmpty(params.getFirst("type")) != null) {
            options.setType(params.getFirst("type"));
        }
        if (nullIfEmpty(params.getFirst("starttime")) != null) {
            options.setStartTime(DateUtil.getStartTime(params.getFirst("starttime")));
        }
        if (nullIfEmpty(params.getFirst("endtime")) != null) {
            options.setEndTime(DateUtil.getEndTime(params.getFirst("endtime")));
        }
        if (nullIfEmpty(params.getFirst("source")) != null) {
            options.setSource(Integer.valueOf(params.getFirst("source")));
        }
        if (nullIfEmpty(params.getFirst("level")) != null) {
            options.setType(params.getFirst("level"));
        }
        return options;
    }

}
