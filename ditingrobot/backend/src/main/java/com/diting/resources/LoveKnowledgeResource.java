package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.core.Universe;
import com.diting.model.love.LoveKnowledge;
import com.diting.service.love.LoveKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by shenkun on 2017/3/6.
 */
@Path("/love")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class LoveKnowledgeResource {
    @Autowired
    private LoveKnowledgeService loveKnowledgeService;

    @POST
    @Timed
    @Path("/create")
    public LoveKnowledge create(LoveKnowledge loveKnowledge){
        return loveKnowledgeService.create(loveKnowledge);
    }

    @GET
    @Timed
    @Path("/{id}")
    public LoveKnowledge get(@PathParam("id") Integer id){
        return loveKnowledgeService.selectById(id);
    }

    @GET
    @Timed
    @Path("/findAll")
    public List<LoveKnowledge> findAll(){
        return loveKnowledgeService.findAll();
    }
}
