package com.diting.service;

import com.diting.model.ChatStatistics;
import com.diting.model.mongo.ChatLog;

import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */
public interface ChatStatisticsService {
    ChatStatistics create(ChatStatistics chatStatistics);
    void deleteAll();
    List<ChatLog> getAllRanking();
}
