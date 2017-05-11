package com.diting.service.impl;

import com.diting.dao.AccountLogMapper;
import com.diting.model.*;
import com.diting.model.options.AccountLogOptions;
import com.diting.model.result.Results;
import com.diting.service.AccountLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings("ALL")
@Service("accountLogService")
@Transactional
public class AccountLogServiceImpl implements AccountLogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountLogServiceImpl.class);

    @Autowired
    private AccountLogMapper accountLogMapper;

    @Override
    public void create(AccountLog accountLog){
        accountLogMapper.create(accountLog);
    }

    @Override
    public Results<AccountLog> searchForPage(AccountLogOptions options) {
        Results results = new Results();
        List<AccountLog> accounts = accountLogMapper.searchForPage(options);
        results.setItems(accounts);
        results.setTotal(options.getTotalRecord());
        return results;
    }

    @Override
    public List<AccountLog> search(AccountLogOptions options) {
        return accountLogMapper.search(options);
    }
}
