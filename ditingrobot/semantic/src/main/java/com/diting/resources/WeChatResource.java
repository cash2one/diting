package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.core.Universe;
import com.diting.model.Account;
import com.diting.model.WeChat;
import com.diting.model.WeChatAccount;
import com.diting.model.options.ChatLogOptions;
import com.diting.model.options.WeChatAccountOptions;
import com.diting.model.wechat.AcceptParams;
import com.diting.model.wechat.Authorization;
import com.diting.service.ChatLogMongoService;
import com.diting.service.WeChatService;
import com.diting.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Map;

import static com.diting.util.DateUtil.getEndTime;
import static com.diting.util.DateUtil.getStartTime;
import static com.diting.util.Utils.buildPageableOptions;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * WeChatResource
 */
@Path("/wechat")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class WeChatResource {

    @Autowired
    WeChatService weChatService;
    @Autowired
    ChatLogMongoService chatLogMongoService;

    @POST
    @Timed
    @Path("/create")
    public WeChatAccount create(WeChatAccount weChatAccount){
        return weChatService.create(weChatAccount);
    }

    @POST
    @Timed
    @Path("/")
    public Response getInfo(WeChat weChat) throws Exception {
        weChatService.getInfo(weChat);
        return Response.status(200).build();
    }
    @POST
    @Timed
    @Path("/authorize")
    public String authorize(Account account) throws Exception {
        return weChatService.authorize(account);
    }
    @POST
    @Timed
    @Path("/redirect")
    public Response redirect(Authorization authorization) throws Exception {
        weChatService.redirect(authorization);
        return Response.status(200).build();
    }
    @POST
    @Timed
    @Path("/accept")
    public String accept(AcceptParams acceptParams) throws Exception {
        return weChatService.accept(acceptParams);
    }
    @GET
    @Timed
    @Path("/search-all-count-time")
    public List<Map<String,Object>> searchAllCountByTime(@Context UriInfo uriInfo){
        return  weChatService.searchAllCountByTime(buildChatRecordOptions(uriInfo));
    }
    @GET
    @Timed
    @Path("/sta_wechat_chat_log")
    public Map<String,Object> staWeChatChatLog(@Context UriInfo uriInfo){
        return  chatLogMongoService.staWeChatChatLog();
    }
    private WeChatAccountOptions buildChatRecordOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        WeChatAccountOptions options = new WeChatAccountOptions();
        buildPageableOptions(uriInfo, options);
        if (nullIfEmpty(params.getFirst("type")) != null){
            options.setType(params.getFirst("type"));
        }
        if (nullIfEmpty(params.getFirst("starttime")) != null) {
            options.setStartTime(DateUtil.getStartTime(params.getFirst("starttime")));
        }
        if (nullIfEmpty(params.getFirst("endtime")) != null) {
            options.setEndTime(DateUtil.getEndTime(params.getFirst("endtime")));
        }
        return options;
    }
}
