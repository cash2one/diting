package com.diting.dao;

import com.diting.model.mongo.ChatLog;
import com.diting.model.options.ChatLogOptions;

import java.util.List;


/**
 * InvalidQuestionMapper
 */
public interface InvalidQuestionMapper {

    void create(ChatLog chatLog);

    void delete(ChatLog chatLog);

    void update(ChatLog chatLog);

    List<ChatLog> searchForPage(ChatLogOptions chatLogOptions);

    List<ChatLog> searchAdminForPage(ChatLogOptions chatLogOptions);

    List<ChatLog> searchForMobile(ChatLogOptions chatLogOptions);

    void batchDelete(List<ChatLog> chatLogs);
}
