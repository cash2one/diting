package com.diting.service;

import com.diting.model.StatisticalData;
import com.diting.model.mongo.ChatLog;
import com.diting.model.options.ChatLogOptions;
import com.diting.model.result.Mail_phone;
import com.diting.model.result.Results;
import com.diting.model.views.ChatLogViews;
import com.diting.model.views.QuestionAndAnswerUserViews;
import com.diting.service.mongo.BaseMongoService;

import java.util.List;
import java.util.Map;

/**
 * ChatLogMongoService
 */
public interface ChatLogMongoService extends BaseMongoService<ChatLog> {
    Results<ChatLog> searchForPage(ChatLogOptions chatRecordOptions);

    List<ChatLog> getChatLogs(ChatLogOptions options);

    Results<ChatLog> searchGroupForPage(ChatLogOptions options);

    List<ChatLog> getAllRanking();

    List<ChatLog> getTopFiftyRanking();

    List<ChatLog> getYesterdayRanking();

    void updateByUsername(ChatLog chatLog);

    List<ChatLog> getAllChatlog();

    int searchYesterdayVisitorsNum();

    int getYesterdayAskNum();

    int getYesterdayUnknownQuestionNum();

    StatisticalData searchAllQuestionCountByUsername(String username);

    int searchAllCountGroupByUUIDWhereByUsername(String username);

    List<Map<String,Object>> searchAllCountGroupByUUIDWhereByUsernameAndTime(ChatLogOptions chatRecordOptions);

    List<Map<String,Object>> searchAllCountByUsernameAndTime(ChatLogOptions chatRecordOptions);

    List<ChatLogViews> searchAllCountByTime(String type);
    List<QuestionAndAnswerUserViews> searchQuestionAndAnswerUserByTime(String type);
    Map<String,Object> staWeChatChatLog();
}
