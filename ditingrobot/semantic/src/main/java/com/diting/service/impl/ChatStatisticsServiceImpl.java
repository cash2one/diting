package com.diting.service.impl;

import com.diting.core.Universe;
import com.diting.dao.ChatStatisticsMapper;
import com.diting.model.*;
import com.diting.model.mongo.ChatLog;
import com.diting.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.diting.util.Utils.isEmpty;

/**
 * Created by Administrator on 2016/12/20.
 */
@SuppressWarnings("ALL")
@Service("chatStatisticsService")
public class ChatStatisticsServiceImpl implements ChatStatisticsService{

    @Autowired
    private ChatStatisticsMapper chatStatisticsMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RobotService robotService;

    @Autowired
    private FansService fansService;

    @Override
    public ChatStatistics create(ChatStatistics chatStatistics) {
        chatStatisticsMapper.create(chatStatistics);
        return chatStatistics;
    }

    @Override
    public void deleteAll() {
        chatStatisticsMapper.deleteAll();
    }

    @Override
    public List<ChatLog> getAllRanking() {
        List<ChatStatistics> chatStatisticsList=chatStatisticsMapper.getAllRanking();
        Integer userId= Universe.current().getUserId();
        List<ChatLog> chatLogs=new ArrayList<>();
        for (ChatStatistics chatStatistics:chatStatisticsList){
            ChatLog chatLog=new ChatLog();
            Company company=companyService.get(chatStatistics.getCompanyId());
            Account account=accountService.getByCompanyId(chatStatistics.getCompanyId());
            if (null!=userId){
                Fans fans=new Fans();
                fans.setOwn_phone(String.valueOf(account.getId()));
                List<Fans> fansList=fansService.search_my_fans(fans);
                chatLog.setAttentionState("false");
                for (Fans fans1:fansList){
                    if (!isEmpty(fans1.getOth_phone())&&fans1.getOth_phone().equals(String.valueOf(userId))){
                        chatLog.setAttentionState("true");
                        break;
                    }
                }
            }
            if(null==account){
                continue;
            }
            chatLog.setHeadImgUrl(account.getHeadImgUrl());
            chatLog.setUserId(account.getId());
            Robot robot=robotService.getByUserId(account.getId());
            chatLog.setRobotValue(chatStatistics.getRobotValue());
            chatLog.setUsername(account.getUserName());
            chatLog.setNum(chatStatistics.getAllCount());
            if (null!=company ||null!=robot) {
                chatLog.setCompanyName(company.getName());
                chatLog.setApp_key(robot.getUniqueId());
                chatLog.setRobotName(robot.getName());
                if (isEmpty(company.getName())){
                    chatLog.setCompanyName(account.getUserName());
                }
                if (isEmpty(robot.getName())){
                    chatLog.setRobotName(account.getUserName());
                }
            } else {
                continue;
            }
            chatLogs.add(chatLog);
        }
        return chatLogs;
    }
}
