package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.core.Universe;
import com.diting.model.StatisticalData;
import com.diting.model.mongo.ChatLog;
import com.diting.model.options.ChatLogOptions;
import com.diting.model.result.Results;
import com.diting.model.views.ChatLogViews;
import com.diting.model.views.QuestionAndAnswerUserViews;
import com.diting.service.ChatLogMongoService;
import com.diting.service.ChatStatisticsService;
import com.diting.service.InvalidQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.List;
import java.util.Map;

import static com.diting.util.DateUtil.getEndTime;
import static com.diting.util.DateUtil.getStartTime;
import static com.diting.util.Utils.buildPageableOptions;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * Created by liufei on 2016/8/22.
 */

@Path("/chat/record")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ChatLogResource {

    @Autowired
    private ChatLogMongoService chatLogMongoService;

    @Autowired
    private InvalidQuestionService invalidQuestionService;

    @Context
    private HttpServletRequest servletRequest;

    @Autowired
    private ChatStatisticsService chatStatisticsService;

    @GET
    @Timed
    @Path("/search-page")
    public Results<ChatLog> searchForPage(@Context UriInfo uriInfo) {
        return chatLogMongoService.searchForPage(buildChatRecordOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/group/search-page")
    public Results<ChatLog> searchGroupForPage(@Context UriInfo uriInfo) {
        return chatLogMongoService.searchGroupForPage(buildChatRecordOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/invalid-question/search-page")
    public Results<ChatLog> searchInvalidQuestionForPage(@Context UriInfo uriInfo) {
        return  invalidQuestionService.findChatLogByUserName(buildChatRecordOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/admin/invalid-question/search-page")
    public Results<ChatLog> searchAdminInvalidQuestionForPage(@Context UriInfo uriInfo) {
        return  invalidQuestionService.findChatLogs(buildChatRecordOptions(uriInfo));
    }

    @PUT
    @Timed
    @Path("/invalid-question/update")
    public Response update(ChatLog chatLog) {
        invalidQuestionService.update(chatLog);
        return Response.status(200).build();
    }

    @POST
    @Timed
    @Path("/update_username")
    public Response updateUsername(ChatLog chatLog) {
        chatLogMongoService.updateByUsername(chatLog);
        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/invalid-question/batchdelete")
    public Response batchDelete(@Context UriInfo uriInfo) {
        invalidQuestionService.delete(buildChatRecordOptions(uriInfo));
        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/all/ranking")
    public List<ChatLog> allRanking() {
        return chatStatisticsService.getAllRanking();
//        return chatLogMongoService.getAllRanking();
    }

    @GET
    @Timed
    @Path("/top/fifty/ranking")
    public List<ChatLog> topRanking() {
        return chatLogMongoService.getTopFiftyRanking();
    }

    @GET
    @Timed
    @Path("/all/yesterday-ranking")
    public List<ChatLog> yesterdayRanking() {
        return chatLogMongoService.getYesterdayRanking();
    }

    @GET
    @Timed
    @Path("/invalid-question/search-mobile")
    public List<ChatLog> searchForMobile(@Context UriInfo uriInfo){
        return  invalidQuestionService.searchForMobile(buildChatRecordOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/sta/search-question-count/all")
    public StatisticalData searchAllQuestionCountByUsername(){
        return  chatLogMongoService.searchAllQuestionCountByUsername(Universe.current().getUserName());
    }

    @GET
    @Timed
    @Path("/sta/search-uuid-count/all")
    public int searchAllCountGroupByUUIDWhereByUsername(@Context UriInfo uriInfo){
        return  chatLogMongoService.searchAllCountGroupByUUIDWhereByUsername(Universe.current().getUserName());
    }

    @GET
    @Timed
    @Path("/sta/search-uuid-count/type")
    public List<Map<String,Object>> searchAllCountGroupByUUIDWhereByUsernameAndTime(@Context UriInfo uriInfo){
        return chatLogMongoService.searchAllCountGroupByUUIDWhereByUsernameAndTime(buildChatRecordOptions(uriInfo));
    }
    //问答量统计
    @GET
    @Timed
    @Path("/sta/search-username-count/type")
    public List<Map<String,Object>> searchAllCountGroupByUsernameWhereByUsernameAndTime(@Context UriInfo uriInfo){
        return chatLogMongoService.searchAllCountByUsernameAndTime(buildChatRecordOptions(uriInfo));
    }
    //问答用户数
    @GET
    @Timed
    @Path("/search-question-answer-user/{type}")
    public List<QuestionAndAnswerUserViews> searchQuestionAndAnswerUserByTime(@PathParam("type") String type){
        return chatLogMongoService.searchQuestionAndAnswerUserByTime(type);
    }

    @GET
    @Timed
    @Path("/sta/search-question-answer-count/{type}")
    public List<ChatLogViews> searchAllCountByTime(@PathParam("type") String type){
        return chatLogMongoService.searchAllCountByTime(type);
    }

    private ChatLogOptions buildChatRecordOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        ChatLogOptions options = new ChatLogOptions();
        buildPageableOptions(uriInfo, options);
        options.setUserName(nullIfEmpty(Universe.current().getUserName()));
        options.setReduceFunction("function(doc, prev){prev.count+=1,prev.extra4=doc.extra4,prev.createdTime=doc.createdTime,prev.loginUsername=doc.loginUsername}");
        if (nullIfEmpty(params.getFirst("uuid")) != null) {
            options.setUuid(params.getFirst("uuid"));
        }
        if (nullIfEmpty(params.getFirst("type")) != null){
            options.setType(params.getFirst("type"));
        }
        if (nullIfEmpty(params.getFirst("question")) != null){
            options.setQuestions(params.get("question"));
        }
        if (nullIfEmpty(params.getFirst("id")) != null){
            options.setIds(params.get("id"));
        }
        if (nullIfEmpty(params.getFirst("keywords")) != null){
            options.setQuestion(params.getFirst("keywords"));
        }
        if (nullIfEmpty(params.getFirst("starttime")) != null) {
            options.setStartTime(getStartTime(params.getFirst("starttime")));
        }
        if (nullIfEmpty(params.getFirst("endtime")) != null) {
            options.setEndTime(getEndTime(params.getFirst("endtime")));
        }
        return options;
    }
}
