package com.diting.service;

import com.diting.model.Account;
import com.diting.model.StaAccount;
import com.diting.model.options.AccountOptions;
import com.diting.model.options.PageableOptions;
import com.diting.model.result.Results;

import java.util.List;
import java.util.Map;

/**
 * AccountService.
 */
public interface StaAccountService {
    StaAccount create(StaAccount staAccount);

    StaAccount update(StaAccount staAccount);

    Results<StaAccount> searchForPage(PageableOptions pageableOptions);

    List<StaAccount> findAllStaAccount();
}
