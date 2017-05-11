package com.diting.job.task;

import com.diting.model.Account;
import com.diting.model.options.AccountOptions;
import com.diting.service.AccountService;
import com.diting.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * DemoTask
 */
public class DemoTask extends BaseTask {

    @Value("${task.demo.enabled}")
    private boolean enabled;

    @Autowired
    protected AccountService accountService;

    public void execute() {
        super.login();

        if (!enabled) {
            LOGGER.info("demo is not enabled");
            return;
        }

        safeRun(new Callback() {
            public void apply() {
                LOGGER.info("DemoTask start process");

                process();

                LOGGER.info("DemoTask success process");
            }
        });
    }

    private void process() {
        AccountOptions options = new AccountOptions();

        List<Account> accounts = accountService.search(options);

        for (Account account : accounts) {
            try {
                System.out.println(account.getUserName());
            } catch (Exception e) {
                LOGGER.info("error occurred during process account [" + account.getId() + "]" + e.getMessage(), e);
            }
        }
    }
}
