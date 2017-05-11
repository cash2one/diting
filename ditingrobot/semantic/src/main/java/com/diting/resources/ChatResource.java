package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Chat;
import com.diting.model.mongo.ChatLog;
import com.diting.model.options.ChatLogOptions;
import com.diting.service.ChatLogMongoService;
import com.diting.service.ChatService;
import com.diting.util.HessianClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static com.diting.util.Utils.nullIfEmpty;

/**
 * ChatResource
 */
@Path("/chats")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ChatResource extends HessianClient {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatLogMongoService chatLogMongoService;

    @POST
    @Timed
    @Path("/questions/answers")
    public Chat questionAnswering(Chat chat) {
        return chatService.getChat(chat);
    }

    @POST
    @Timed
    @Path("/remote/questions/answers")
    public Chat remoteQuestionAnswering(Chat chat) {
        return chatService.getRemoteChat(chat);
    }

    @POST
    @Timed
    @Path("/angel/questions/answers")
    public Chat angelQuestionAnswering(Chat chat) {
        return chatService.getAngelChat(chat);
    }

    @POST
    @Timed
    @Path("/chatlog/search")
    public List<ChatLog> getChatLogs(@Context UriInfo uriInfo) {
        return chatLogMongoService.getChatLogs(buildChatLogOptions(uriInfo));
    }

    private ChatLogOptions buildChatLogOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        ChatLogOptions options = new ChatLogOptions();
        options.setUserName(nullIfEmpty(params.getFirst("userName")));

        return options;
    }

}
