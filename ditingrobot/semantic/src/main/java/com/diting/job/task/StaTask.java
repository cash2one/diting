package com.diting.job.task;

import com.diting.model.StaAccount;
import com.diting.model.options.AccountOptions;
import com.diting.model.options.KnowledgeOptions;
import com.diting.service.AccountService;
import com.diting.service.KnowledgeService;
import com.diting.service.StaAccountService;
import com.diting.service.StaChatLogService;
import com.diting.util.Callback;
import com.diting.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * StaTask
 */
public class StaTask extends BaseTask {

    @Value("${task.sta.enabled}")
    private boolean enabled;

    @Autowired
    protected AccountService accountService;
    @Autowired
    protected StaAccountService staAccountService;
    @Autowired
    protected StaChatLogService staChatLogService;
    @Autowired
    protected KnowledgeService knowledgeService;

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

    private void process() {
        StaAccount staAccount = getAddAccount();
        staAccount.setDayLoginCount(getDayLoginCount());
        Long dayValidCount = staChatLogService.searchDayValidCount();
//        Long dayInvalidCount = staChatLogService.searchDayInvalidCount();
        Long dayQuestionCount = staChatLogService.searchDayQuestionCount();
        Integer dayQuestionAndAnswerNumber=staChatLogService.searchQuestionAndAnswerNumber();
        Integer dayQuestionAndAnswerUserNum=staChatLogService.searchQuestionAndAnswerUserNum();
        staAccount.setDayValidCount(dayValidCount.intValue());
        staAccount.setDayInvalidCount(dayQuestionCount.intValue()-dayValidCount.intValue());
        staAccount.setDayQuestionCount(dayQuestionCount.intValue());
        staAccount.setDayKnowledgeCount(getDayKnowledgeCount());
        staAccount.setDayQuestionAndAnswerNumber(dayQuestionAndAnswerNumber);
        staAccount.setDayQuestionAndAnswerUserNum(dayQuestionAndAnswerUserNum);
        staAccountService.create(staAccount);
    }

    private StaAccount getAddAccount() {
        AccountOptions options = new AccountOptions();
        options.setBeginTime(Utils.date2str(Utils.daysBefore(Utils.today(), 1)));
        options.setEndTime(Utils.date2str(Utils.today()));
        Integer count = accountService.searchAddAccount(options);
        options.setSource("1");
        Integer one_count=accountService.searchAddAccountBySource(options);
        options.setSource("2");
        Integer two_count=accountService.searchAddAccountBySource(options);
        options.setSource("3");
        Integer three_count=accountService.searchAddAccountBySource(options);
        options.setSource("4");
        Integer four_count=accountService.searchAddAccountBySource(options);
        options.setSource("5");
        Integer five_count=accountService.searchAddAccountBySource(options);
        StaAccount staAccount = new StaAccount();
        staAccount.setDayAccountCount(count);
        staAccount.setDataTime(Utils.daysBefore(Utils.now(),1));
        staAccount.setDayAccountCountOne(one_count);
        staAccount.setDayAccountCountTwo(two_count);
        staAccount.setDayAccountCountThree(three_count);
        staAccount.setDayAccountCountFour(four_count);
        staAccount.setDayAccountCountFive(five_count);
        return staAccount;
    }

    private Integer getDayLoginCount() {
        AccountOptions options = new AccountOptions();
        options.setBeginTime(Utils.date2str(Utils.daysBefore(Utils.today(), 1)));
        options.setEndTime(Utils.date2str(Utils.today()));
        return accountService.searchDayLoginAccount(options);
    }

    private Integer getDayKnowledgeCount() {
        KnowledgeOptions options = new KnowledgeOptions();
        options.setStartTime(Utils.date2str(Utils.daysBefore(Utils.today(), 1)));
        options.setEndTime(Utils.date2str(Utils.today()));
        return knowledgeService.searchKnowledgeCount(options);
    }

}
