package com.diting.service;

import com.diting.model.Account;
import com.diting.model.AccountLog;
import com.diting.model.options.AccountLogOptions;
import com.diting.model.result.Results;

import java.util.List;

/**
 * AccountLogService.
 */
public interface AccountLogService {
    void create(AccountLog accountLog);

    Results<AccountLog> searchForPage(AccountLogOptions options);

    List<AccountLog> search(AccountLogOptions options);

}
