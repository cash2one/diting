package com.diting.service.impl;

import com.diting.dao.mongo.ChatLogMapper;
import com.diting.model.mongo.ChatLog;
import com.diting.service.StaChatLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * StaChatLogServiceImpl.
 */

@Service("staChatLogService")
@Transactional
public class StaChatLogServiceImpl implements StaChatLogService {
    @Autowired
    private ChatLogMapper chatLogMapper;

    @Override
    public long searchDayValidCount() {
        return chatLogMapper.searchDayValidCount();
    }

    @Override
    public long searchDayInvalidCount() {
        return chatLogMapper.searchDayInvalidCount();
    }

    @Override
    public long searchDayQuestionCount() {
        return chatLogMapper.searchDayQuestionCount();
    }

    @Override
    public long searchDayQuestionCountByUsername(String username) {
        return chatLogMapper.searchDayQuestionCountByUsername(username);
    }

    @Override
    public int searchQuestionAndAnswerNumber() {
        return chatLogMapper.searchQuestionAndAnswerNumber();
    }

    @Override
    public int searchQuestionAndAnswerUserNum() {
        return chatLogMapper.searchQuestionAndAnswerUserNum();
    }

    @Override
    public List<ChatLog> searchChatLogByChargeMode(String username) {
        return chatLogMapper.searchChatLogByChargeMode(username);
    }
}
