package com.diting.service;

import com.diting.model.FeedbackMessage;
import com.diting.model.Message;
import com.diting.model.options.MessageOptions;
import com.diting.model.result.Results;

/**
 * Created by liufei on 2016/10/11.
 */
public interface MessageService {
    Message create(Message message);

    FeedbackMessage createFeedbackMessage(FeedbackMessage feedbackMessage);

    Results<Message> searchMessageForPage(MessageOptions options);

    Message searchByMessageId(Integer id);

    void delete(MessageOptions options);
}
