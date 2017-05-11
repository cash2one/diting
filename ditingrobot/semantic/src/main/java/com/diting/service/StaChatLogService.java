package com.diting.service;


import com.diting.model.mongo.ChatLog;

import java.util.List;

/**
 * ChatLogStaService
 */
public interface StaChatLogService {
    long searchDayValidCount();

    long searchDayInvalidCount();

    long searchDayQuestionCount();

    long searchDayQuestionCountByUsername(String username);

    int searchQuestionAndAnswerNumber();

    int searchQuestionAndAnswerUserNum();

    List<ChatLog> searchChatLogByChargeMode(String username);
}
