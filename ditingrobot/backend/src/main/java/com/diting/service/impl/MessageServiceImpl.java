package com.diting.service.impl;

import com.diting.core.Universe;
import com.diting.dao.AccountMapper;
import com.diting.dao.FeedbackMessageMapper;
import com.diting.dao.MessageMapper;
import com.diting.model.Account;
import com.diting.model.FeedbackMessage;
import com.diting.model.Message;
import com.diting.model.options.MessageOptions;
import com.diting.model.result.Results;
import com.diting.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
@SuppressWarnings("ALL")
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private FeedbackMessageMapper feedbackMessageMapper;

    @Override
    public Message create(Message message) {
        message.setUserId(Universe.current().getUserId());
        Account account = accountMapper.get(Universe.current().getUserId());
        message.setCompanyId(account.getCompanyId());
        messageMapper.create(message);
        return message;
    }

    @Override
    public FeedbackMessage createFeedbackMessage(FeedbackMessage feedbackMessage) {
        feedbackMessage.setAdminName(Universe.current().getUserName());
        feedbackMessageMapper.create(feedbackMessage);
        return feedbackMessage;
    }

    @Override
    public Results<Message> searchMessageForPage(MessageOptions options) {
        Results results = new Results();
        List<Message> messages = messageMapper.searchMessageForPage(options);
        results.setItems(messages);
        results.setTotal(options.getTotalRecord());
        return results;
    }

    @Override
    public Message searchByMessageId(Integer id) {
        List<Message> messageList=messageMapper.searchByMessageId(id);
//        return messageMapper.searchByMessageId(message);
        if (messageList!=null&&messageList.size()>0){
            return messageList.get(0);
        }else {
            return messageMapper.searchMessage(id);
        }

    }

    @Override
    public void delete(MessageOptions options) {
        List<Message> messages = new ArrayList<>();
        List<String> ids = options.getIds();
        for (int i = 0; i < ids.size(); i++) {
            Message message = new Message();
            message.setId(Integer.valueOf(ids.get(i)));
            messages.add(message);
        }
        messageMapper.batchDelete(messages);
    }
}
