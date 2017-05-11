package com.diting.service.impl;

import com.diting.core.Universe;
import com.diting.model.Home;
import com.diting.model.options.KnowledgeOptions;
import com.diting.service.ChatLogMongoService;
import com.diting.service.HomePageService;
import com.diting.service.KnowledgeService;
import com.diting.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/4/12.
 */
@Service
public class HomPageServiceImpl implements HomePageService{

    @Autowired
    private ChatLogMongoService chatLogMongoService;

    @Autowired
    private KnowledgeService knowledgeService;

    @Override
    public Home findHome() {
        Home home=new Home();
        home.setYesterdayVisitorsNum(getYesterdayVisitorsNum());
        home.setYesterdayAskNum(getYesterdayAskNum());
        home.setYesterdayUnknownQuestionNum(getYesterdayUnknownQuestionNum());
        home.setYesterdayNewKnowledgeNum(getYesterdayNewKnowledgeNum());
        return home;
    }

    @Override
    public Home findYesterdayNewKnowledgeNum() {
        Home home=new Home();
        home.setYesterdayNewKnowledgeNum(getYesterdayNewKnowledgeNum());
        return home;
    }

    private int getYesterdayVisitorsNum(){
        return chatLogMongoService.searchYesterdayVisitorsNum();
    }

    private int getYesterdayAskNum(){
        return chatLogMongoService.getYesterdayAskNum();
    }
    private int getYesterdayUnknownQuestionNum(){
        return chatLogMongoService.getYesterdayUnknownQuestionNum();
    }

    private int getYesterdayNewKnowledgeNum(){
        KnowledgeOptions options = new KnowledgeOptions();
        options.setStartTime(Utils.date2str(Utils.daysBefore(Utils.today(), 1)));
        options.setEndTime(Utils.date2str(Utils.today()));
        options.setAccountId(Universe.current().getUserId());
        return knowledgeService.searchKnowledgeCountByAccountId(options);
    }

}
