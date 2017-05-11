package com.diting.job.task;

import com.diting.model.Account;
import com.diting.model.Order;
import com.diting.model.Wallet;
import com.diting.model.enums.UserType;
import com.diting.model.enums.WalletLotType;
import com.diting.model.mongo.ChatLog;
import com.diting.model.options.WalletSearchOptions;
import com.diting.resources.request.CreditWalletRequest;
import com.diting.resources.request.DebitWalletRequest;
import com.diting.service.*;
import com.diting.util.Callback;
import com.diting.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.List;

import static com.diting.util.Utils.*;

/**
 * CalculateTask
 */
public class CalculateTask extends BaseTask {

    private final static String START_DATE = "2016-12-02 00:00:00";

    @Value("${task.calculate.enabled}")
    private boolean enabled;

    @Autowired
    protected AccountService accountService;
    @Autowired
    protected StaAccountService staAccountService;
    @Autowired
    protected StaChatLogService staChatLogService;
    @Autowired
    protected KnowledgeService knowledgeService;

    @Autowired
    protected WalletService walletService;

    public void execute() {
        super.login();

        if (!enabled) {
            LOGGER.info("CalculateTask is not enabled");
            return;
        }

        safeRun(new Callback() {
            public void apply() {
                LOGGER.info("CalculateTask start process");

                process();

                LOGGER.info("CalculateTask success process");
            }
        });
    }

    private void process() {
        if (now().compareTo(str2Date(START_DATE)) > 0) {
            List<Account> accounts = accountService.search(null);
            for (Account account : accounts) {
                //获取聊天记录数量
                Long dayQuestionCount = staChatLogService.searchDayQuestionCountByUsername(account.getUserName());
                Integer count = dayQuestionCount.intValue();
                LOGGER.info("Chat accounts [" + account.getMobile() + "] for a total of [" + count + "].");
                if (count == 0) continue;
                consumption(account,count);
                List<ChatLog> chatLogList=staChatLogService.searchChatLogByChargeMode(account.getUserName());
                if (null!=chatLogList&&chatLogList.size()>0){
                    int xperts_num=0;
                    for (ChatLog chatLog:chatLogList){
                         if (!isEmpty(chatLog.getDataSource())&&chatLog.getDataSource().equals("B")){
                             xperts_num+=1;
                         }
                         if (!isEmpty(chatLog.getLoginUsername())){
                             String username=account.getUserName();
                             account.setUserName(chatLog.getLoginUsername());
                             consumption(account,1);
                             account.setUserName(username);
                         }
                    }
                    if (xperts_num>0){
                        charge(account,xperts_num);
                    }
                }
            }
        }
    }
    //消费
    private void consumption(Account account,int count){
        //获取个人账户信息
        WalletSearchOptions options = new WalletSearchOptions();
        options.setUserId(str(account.getId()));
        options.setUserType(UserType.DITING_USER.toString());
        Wallet wallet = Utils.getFirst(walletService.search(options));

        //更新扣费信息
        DebitWalletRequest debitWalletRequest = new DebitWalletRequest();
        debitWalletRequest.setWalletId(wallet.getId());
        debitWalletRequest.setAmount(new BigDecimal(count));
        debitWalletRequest.setLotType(str(WalletLotType.DIBI_D));
        debitWalletRequest.setReason("聊天消费");
        debitWalletRequest.setEvent("聊天消费");
        walletService.debit(debitWalletRequest);

        //判断更新后的账户余额 如果余额小于0 账户禁用
        wallet = walletService.get(wallet.getId());
        if (wallet.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            //禁用
            account.setForbiddenEnable(true);
            accountService.update(account);
        }
    }

    //收费
    private void charge(Account account,int count){
        WalletSearchOptions walletSearchOptions=new WalletSearchOptions();
        walletSearchOptions.setUserId(String.valueOf(account.getId()));
        Wallet wallet=walletService.search(walletSearchOptions).get(0);
        CreditWalletRequest creditWalletRequest=new CreditWalletRequest();
        creditWalletRequest.setAmount(new BigDecimal(count));//充值点数
        creditWalletRequest.setEvent("收费");
        creditWalletRequest.setLotType(String.valueOf(WalletLotType.DIBI_D));
        creditWalletRequest.setReason("专家模式收费");
        creditWalletRequest.setWalletId(wallet.getId());
        walletService.credit(creditWalletRequest);
    }

}
