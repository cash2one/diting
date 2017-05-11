package com.diting.job.task;

import com.diting.model.Account;
import com.diting.model.Wallet;
import com.diting.notification.notifier.NotifierContext;
import com.diting.notification.notifier.PaymentNotifier;
import com.diting.service.AccountService;
import com.diting.service.WalletService;
import com.diting.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.List;

/**
 * PaymentTask
 */
public class PaymentTask extends BaseTask {
    @Value("${task.payment.enabled}")
    private boolean enabled;

    @Autowired
    private PaymentNotifier paymentNotifier;

    @Autowired
    protected AccountService accountService;
    @Autowired
    protected WalletService walletService;


    public void execute() {
        super.login();

        if (!enabled) {
            LOGGER.info("PaymentTask is not enabled");
            return;
        }

        safeRun(new Callback() {
            public void apply() {
                LOGGER.info("PaymentTask start process");

                process();

                LOGGER.info("PaymentTask success process");
            }
        });
    }

    private void process() {
        List<Wallet> wallets = walletService.search(null);
        for (Wallet wallet : wallets) {
            if (wallet.getBalance().compareTo(new BigDecimal(0)) <= 0) {
                Account account = accountService.get(wallet.getUserId());
                NotifierContext context = new NotifierContext();
                context.setMobile(account.getMobile());
                LOGGER.info("notifier account [" + context.getMobile() + "] balance needed payment. ");
                paymentNotifier.notify(context);
            }
        }
    }

}
