package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Account;
import com.diting.model.AccountLog;
import com.diting.model.options.AccountLogOptions;
import com.diting.model.options.AccountOptions;
import com.diting.model.result.Results;
import com.diting.resources.request.AccountBindRequest;
import com.diting.resources.request.ChangePasswordRequest;
import com.diting.resources.request.ResetPasswordRequest;
import com.diting.service.AccountLogService;
import com.diting.service.AccountService;
import com.diting.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

import static com.diting.util.Utils.*;

/**
 * AccountLogResource
 */
@Path("/accountLog")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class AccountLogResource {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountLogService accountLogService;

    @GET
    @Timed
    @Path("/search-page")
    public Results<AccountLog> searchForPage(@Context UriInfo uriInfo) {
        return accountLogService.searchForPage(buildAccountOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/search")
    public List<AccountLog> search(@Context UriInfo uriInfo) {
        return accountLogService.search(buildAccountOptions(uriInfo));
    }

    private AccountLogOptions buildAccountOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();
        AccountLogOptions options = new AccountLogOptions();
        buildPageableOptions(uriInfo, options);
        if (nullIfEmpty(params.getFirst("starttime")) != null) {
            options.setStartTime(DateUtil.getStartTime(params.getFirst("starttime")));
        }
        if (nullIfEmpty(params.getFirst("endtime")) != null) {
            options.setEndTime(DateUtil.getEndTime(params.getFirst("endtime")));
        }
        if (nullIfEmpty(params.getFirst("reason")) != null) {
            options.setReason(params.getFirst("reason"));
        }
        if (nullIfEmpty(params.getFirst("content")) != null) {
            options.setContent(params.getFirst("content"));
        }
        return options;
    }
}
