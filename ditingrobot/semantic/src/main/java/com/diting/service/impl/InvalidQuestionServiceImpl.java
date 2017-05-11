package com.diting.service.impl;

import com.diting.core.Universe;
import com.diting.dao.InvalidQuestionMapper;
import com.diting.model.mongo.ChatLog;
import com.diting.model.Knowledge;
import com.diting.model.options.ChatLogOptions;
import com.diting.model.result.Results;
import com.diting.service.InvalidQuestionService;
import com.diting.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * InvalidQuestionServiceImpl.
 */

@SuppressWarnings("ALL")
@Service("invalidQuestionService")
@Transactional
public class InvalidQuestionServiceImpl implements InvalidQuestionService{

    @Autowired
    private InvalidQuestionMapper invalidQuestionMapper;

    @Autowired
    private KnowledgeService knowledgeService;

    @Override
    public void insert(ChatLog chatLog) {
        invalidQuestionMapper.create(chatLog);
    }

    @Override
    public Results<ChatLog> findChatLogByUserName(ChatLogOptions chatLogOptions) {
        Results results = new Results();
        List<ChatLog> chatLogs=invalidQuestionMapper.searchForPage(chatLogOptions);
        results.setItems(chatLogs);
        results.setTotal(chatLogOptions.getTotalRecord());
        return results;
    }

    @Override
    public Results<ChatLog> findChatLogs(ChatLogOptions chatLogOptions) {
        Results results = new Results();
        List<ChatLog> chatLogs=invalidQuestionMapper.searchAdminForPage(chatLogOptions);
        results.setItems(chatLogs);
        results.setTotal(chatLogOptions.getTotalRecord());
        return results;
    }

    @Override
    public List<ChatLog> searchForMobile(ChatLogOptions chatLogOptions) {
        return invalidQuestionMapper.searchForMobile(chatLogOptions);
    }

    @Override
    public void update(ChatLog chatLog) {
        chatLog.setUsername(Universe.current().getUserName());
        invalidQuestionMapper.update(chatLog);
        Knowledge knowledge=this.getKnowledge(chatLog);
        knowledgeService.complexCreate(knowledge);
    }

    @Override
    public void delete(ChatLogOptions chatLogOptions) {
        List<ChatLog> chatLogs=new ArrayList<>();
        List<String> ids=chatLogOptions.getIds();
        for (int i=0;i<ids.size();i++){
            ChatLog chatLog=new ChatLog();
            if (Universe.current().getUserName()==null){
                chatLog.setUsername("13552141271");
            }else {
                chatLog.setUsername(Universe.current().getUserName());
            }
            chatLog.setId(Integer.valueOf(ids.get(i)));
            chatLogs.add(chatLog);
        }
        invalidQuestionMapper.batchDelete(chatLogs);
    }


    public Knowledge getKnowledge(ChatLog chatLog){
        Knowledge knowledge=new Knowledge();
        knowledge.setQuestion(chatLog.getQuestion());
        knowledge.setAnswer(chatLog.getAnswer());
        return knowledge;
    }
}
