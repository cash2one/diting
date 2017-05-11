package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.TeleOther;
import com.diting.model.options.TeleOtherOptions;
import com.diting.model.result.Results;
import com.diting.service.TeleOtherService;
import com.diting.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import static com.diting.util.Utils.buildPageableOptions;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * TeleOtherResource
 */
@Path("/teleOther")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class TeleOtherResource {

    @Autowired
    private TeleOtherService teleOtherService;

    @POST
    @Timed
    @Path("/create")
    public TeleOther create(TeleOther teleOther) {
        return teleOtherService.create(teleOther);
    }

    @POST
    @Timed
    @Path("/update")
    public TeleOther update(TeleOther teleOther) {
        return teleOtherService.update(teleOther);
    }

    @GET
    @Timed
    @Path("/{id}")
    public TeleOther get(@PathParam("id") Integer id) {
        return teleOtherService.get(id);
    }

    @POST
    @Timed
    @Path("/delete/{id}")
    public Boolean delete(@PathParam("id") Integer id) {
        return teleOtherService.delete(id);
    }

    @GET
    @Timed
    @Path("/search")
    public Results<TeleOther> searchBaseKnowledge(@Context UriInfo uriInfo) {
        TeleOtherOptions options = buildTeleOtherOptions(uriInfo);
        return teleOtherService.search(options);
    }

    private TeleOtherOptions buildTeleOtherOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        TeleOtherOptions options = new TeleOtherOptions();
        buildPageableOptions(uriInfo, options);
        if (nullIfEmpty(params.getFirst("id")) != null && !nullIfEmpty(params.getFirst("id")).contains("-1")) {
            options.setId(Integer.parseInt(params.getFirst("id")));
        }
        if (nullIfEmpty(params.getFirst("starttime")) != null) {
            options.setStartTime(DateUtil.getStartTime(params.getFirst("starttime")));
        }
        if (nullIfEmpty(params.getFirst("endtime")) != null) {
            options.setEndTime(DateUtil.getEndTime(params.getFirst("endtime")));
        }
        if (nullIfEmpty(params.getFirst("forword_name")) != null) {
            options.setForword_name(params.getFirst("forword_name"));
        }
        if (nullIfEmpty(params.getFirst("receive_name")) != null) {
            options.setReceive_name(params.getFirst("receive_name"));
        }
        if (nullIfEmpty(params.getFirst("message")) != null) {
            options.setMessage(params.getFirst("message"));
        }
        if (nullIfEmpty(params.getFirst("flag")) != null) {
            options.setMessage(params.getFirst("flag"));
        }
        return options;
    }
}
