package com.diting.service;

import com.diting.model.mongo.ChatLog;
import com.diting.service.mongo.BaseMongoService;

/**
 * Created by liufei on 2016/10/19.
 */
public interface ChatLogCountMongoService extends BaseMongoService<ChatLog> {

    Integer getChatLogCountByUsername(String username);

    Integer getChatLogYesterdayCountByUsername(String username);

    Integer getChatLogInvalidCountByUsername(String username);
}
