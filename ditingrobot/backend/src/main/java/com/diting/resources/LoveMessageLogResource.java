package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.love.LoveKnowledge;
import com.diting.model.love.LoveMessageLog;
import com.diting.service.love.LoveKnowledgeService;
import com.diting.service.love.LoveMessageLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by shenkun on 2017/3/6.
 */
@Path("/loveMessageLog")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class LoveMessageLogResource {
    @Autowired
    private LoveMessageLogService loveMessageLogService;

    @POST
    @Timed
    @Path("/create")
    public LoveMessageLog create(LoveMessageLog loveMessageLog){
        return loveMessageLogService.create(loveMessageLog);
    }

    @GET
    @Timed
    @Path("/updateByUserId")
    public void updateByUserId(){
        loveMessageLogService.updateByUserId();
    }

    @GET
    @Timed
    @Path("/selectCountByUserId")
    public Integer selectCountByUserId(){
        return loveMessageLogService.selectCountByUserId();
    }

    @GET
    @Timed
    @Path("/selectByUserId")
    public List<LoveMessageLog> selectByUserId(){
        loveMessageLogService.selectCountMap();
        return loveMessageLogService.selectByUserId();
    }

    @GET
    @Timed
    @Path("/selectCountByReceiveId/{loveId}")
    public Integer selectCountByReceiveId(@PathParam("loveId") Integer loveId){
        return loveMessageLogService.selectCountByLoveId(loveId);
    }

    @GET
    @Timed
    @Path("/selectCountGroupByReceiveId")
    public List<Map<String, Object>> selectCountGroupByReceiveId(){
        return loveMessageLogService.selectCountMap();
    }
}
