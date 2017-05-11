package com.diting.service;

import com.diting.model.mongo.ChatLog;
import com.diting.model.options.ChatLogOptions;
import com.diting.model.result.Results;

import java.util.List;


/**
 * InvalidQuestionService
 */
public interface InvalidQuestionService {

    void insert(ChatLog chatLog);

    Results<ChatLog> findChatLogByUserName(ChatLogOptions chatLogOptions);

    Results<ChatLog> findChatLogs(ChatLogOptions chatLogOptions);

    List<ChatLog> searchForMobile(ChatLogOptions chatLogOptions);

    void update(ChatLog chatLog);

    void delete(ChatLogOptions chatLogOptions);
}
