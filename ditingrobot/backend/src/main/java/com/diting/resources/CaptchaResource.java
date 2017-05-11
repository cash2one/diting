package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/captchas")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class CaptchaResource {
    @Autowired
    private CaptchaService captchaService;

    @GET
    @Timed
    @Path("/mobile/{mobile}")
    public Response getMobileCaptcha(@PathParam("mobile") String mobile) {
        captchaService.getMobileCaptcha(mobile);
        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/mobile/{mobile}/verify/{verifyCode}")
    public Boolean checkMobileCaptcha(@PathParam("mobile") String mobile, @PathParam("verifyCode") String verifyCode) {
        return captchaService.checkMobileCaptcha(mobile, verifyCode);
    }

}

