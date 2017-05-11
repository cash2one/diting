package com.diting.service.impl;

import com.diting.dao.ChatLogCountMongoMapper;
import com.diting.model.mongo.ChatLog;
import com.diting.service.ChatLogCountMongoService;
import com.diting.service.mongo.BaseMongoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2016/10/19.
 */
@SuppressWarnings("ALL")
@Service("chatLogCountMongoService")
@Transactional
public class ChatLogCountMongoServiceImpl extends BaseMongoServiceImpl<ChatLog> implements ChatLogCountMongoService{

    @Autowired
    private ChatLogCountMongoMapper chatLogCountMongoMapper;

    @Override
    public Integer getChatLogCountByUsername(String username) {
        return chatLogCountMongoMapper.findChatLogCountByUsername(username);
    }

    @Override
    public Integer getChatLogYesterdayCountByUsername(String username) {
        return chatLogCountMongoMapper.findChatLogYesterdayCountByUsername(username);
    }

    @Override
    public Integer getChatLogInvalidCountByUsername(String username) {
        return chatLogCountMongoMapper.findChatLogInvalidCountByUsername(username);
    }

}
