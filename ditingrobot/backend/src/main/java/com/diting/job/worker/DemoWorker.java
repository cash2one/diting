package com.diting.job.worker;

import com.diting.model.Account;
import com.diting.model.options.AccountOptions;
import com.diting.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * DemoWorker
 */
public class DemoWorker extends BaseWorker {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private AccountService accountService;

    @Override
    protected void process() throws Exception {
        // fake login
        super.login();
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