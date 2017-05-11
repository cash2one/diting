package com.diting.job.task;

import com.diting.model.Account;
import com.diting.model.ChatStatistics;
import com.diting.model.Fans;
import com.diting.model.Knowledge;
import com.diting.service.*;
import com.diting.util.Callback;
import com.diting.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */
public class ChatStatisticsTask extends BaseTask {

    @Value("${task.chatstatistics.enabled}")
    private boolean enabled;

    @Autowired
    private ChatLogCountMongoService chatLogCountMongoService;

    @Autowired
    protected AccountService accountService;

    @Autowired
    private ChatStatisticsService chatStatisticsService;

    @Autowired
    private KnowledgeService knowledgeService;

    @Autowired
    private FansService fansService;

    @Autowired
    private WalletService walletService;

    public void execute() {
        super.login();

        if (!enabled) {
            LOGGER.info("sta is not enabled");
            return;
        }

        safeRun(new Callback() {
            public void apply() {
                LOGGER.info("StaTask start process");

                process();

                LOGGER.info("StaTask success process");
            }
        });
    }

    public void process() {
        chatStatisticsService.deleteAll();
        List<Account> accountList = accountService.search(null);
        for (Account account : accountList) {
            ChatStatistics chatStatistics = new ChatStatistics();
            Integer chatLogAccountAll = chatLogCountMongoService.getChatLogCountByUsername(account.getMobile());
            Integer chatLogYesterdayAccount = chatLogCountMongoService.getChatLogYesterdayCountByUsername(account.getMobile());
            Integer chatLogInvalidAccount = chatLogCountMongoService.getChatLogInvalidCountByUsername(account.getMobile());
            chatStatistics.setAllCount(chatLogAccountAll);
            chatStatistics.setUserId(account.getId());
            chatStatistics.setCompanyId(account.getCompanyId());
            chatStatistics.setYesterdayCount(chatLogYesterdayAccount);
            chatStatistics.setTime(DateUtil.getTime());
            double accuracyRate = 0;
            if (!chatLogAccountAll.equals(0)) {
                accuracyRate = (double) chatLogInvalidAccount / chatLogAccountAll;
            }
            chatStatistics.setAccuracyRate(DateUtil.getPercentage(accuracyRate));
            List<Knowledge> knowledgeList = knowledges(account);
            Fans fans=new Fans();
            fans.setOwn_phone(String.valueOf(account.getId()));
            Integer fans_count =  fansService.find_fans_count(fans);
            BigDecimal balance=walletService.getWalletByUserId(account.getId());
            BigDecimal robotVal = robotVal(knowledgeList,accuracyRate,fans_count,balance);
            chatStatistics.setRobotValue(robotVal);
            chatStatisticsService.create(chatStatistics);
        }
        System.out.println("chatStatisticsTask is over");
    }

    private List<Knowledge> knowledges(Account account) {
        Knowledge knowledge = new Knowledge();
        knowledge.setAccountId(account.getId());
        return knowledgeService.getKnowledgeByFrequencr(knowledge);
    }

    private BigDecimal robotVal(List<Knowledge> knowledges,double accuracyRate,Integer fans_count,BigDecimal balance) {
        Integer count = null != knowledges & knowledges.size() > 0 ? knowledges.size() : 0;
        Integer allCount=0;
        for (Knowledge knowledge:knowledges){
            allCount+=knowledge.getFrequency();
        }
        BigDecimal robotVal=BigDecimal.ZERO;
        robotVal= BigDecimal.valueOf((count*100+allCount+fans_count*50+balance.intValue())*accuracyRate/100+50);
        return robotVal;
    }
}
