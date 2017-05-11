package com.diting.dao;

import com.diting.model.Account;
import com.diting.model.AccountLog;
import com.diting.model.options.AccountLogOptions;

import java.util.List;

/**
 * AccountLogMapper.
 */
public interface AccountLogMapper {

    void create(AccountLog accountLog);

    List<AccountLog> searchForPage(AccountLogOptions options);

    List<AccountLog> search(AccountLogOptions options);

}
