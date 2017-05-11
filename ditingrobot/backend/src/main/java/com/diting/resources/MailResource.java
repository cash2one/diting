package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.*;
import com.diting.model.options.MailOptions;
import com.diting.model.result.Flag;
import com.diting.model.result.Results;
import com.diting.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static com.diting.util.Utils.buildPageableOptions;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * AccountResource
 */
@Path("/mails")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class MailResource {

    @Autowired
    private MailService mailService;

    @GET
    @Timed
    @Path("/search")
    public List<Mail> search() {
        return mailService.search();
    }

    @POST
    @Timed
    @Path("/save")
    public Mail save(Mail mail) {
        return mailService.save(mail);
    }






    //查找 本手机号码下有没有未查阅的信息
    //1 没有 没有阅读过的消息
    // 0 有 没有阅读过的消息
    @GET
    @Timed
    @Path("/find_phone")
    public Flag find_phone() {
        return mailService.find_phone();
    }

    @GET
    @Timed
    @Path("/find/unread-letter-num")
    public Flag findUnreadLetterNum() {
        return mailService.findUnreadLetterNum();
    }


    //查找所有的站内信息
    // 如果没有阅读过标记出来
    @GET
    @Timed
    @Path("/search_phone")
    public Results<Mail> search_phone(@Context UriInfo uriInfo) {
        return mailService.search_phone(buildMailOptions(uriInfo));
    }

    //点击过以后 更改阅读过的状态
    @POST
    @Timed
    @Path("/save_phone")
    public Mail_phone save_phone(Mail_phone mail) {
        return mailService.save_phone(mail);
    }

    //点击过以后 更改阅读过的状态
    @POST
    @Timed
    @Path("/find_p")
    public Mail_phone find_p(Mail_phone mail) {
        return mailService.find_p(mail);
    }


    public  MailOptions buildMailOptions(UriInfo uriInfo){
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();
        MailOptions options = new MailOptions();
        buildPageableOptions(uriInfo, options);
        return options;
    }
}
