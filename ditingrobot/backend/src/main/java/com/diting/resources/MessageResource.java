package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.FeedbackMessage;
import com.diting.model.Message;
import com.diting.model.options.MessageOptions;
import com.diting.model.result.Results;
import com.diting.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static com.diting.util.Utils.buildPageableOptions;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * Created by liufei on 2016/10/11.
 */
@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class MessageResource {

    @Autowired
    private MessageService messageService;

    @POST
    @Timed
    @Path("/create")
    public Message create(Message message) {
        return messageService.create(message);
    }

    @POST
    @Timed
    @Path("/feedback_message/create")
    public FeedbackMessage createFeedbackMessage(FeedbackMessage feedbackMessage) {
        return messageService.createFeedbackMessage(feedbackMessage);
    }

    @GET
    @Timed
    @Path("/search-page")
    public Results<Message> searchForPage(@Context UriInfo uriInfo){
        return messageService.searchMessageForPage(buildMessageOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/search-message/{id}")
    public Message searchByMessageId(@PathParam("id") Integer id){
        return messageService.searchByMessageId(id);
    }

    @GET
    @Timed
    @Path("/batchdelete")
    public Response batchDelete(@Context UriInfo uriInfo) {
        messageService.delete(buildMessageOptions(uriInfo));
        return Response.status(200).build();
    }
    private MessageOptions buildMessageOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        MessageOptions options = new MessageOptions();
        buildPageableOptions(uriInfo, options);

        if (nullIfEmpty(params.getFirst("id")) != null){
            options.setIds(params.get("id"));
        }
        return options;
    }
}
