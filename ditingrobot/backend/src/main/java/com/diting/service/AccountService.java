package com.diting.service;

import com.diting.model.Account;
import com.diting.model.options.AccountOptions;
import com.diting.model.result.Results;
import com.diting.model.views.AccountViews;
import com.diting.resources.request.AccountBindRequest;

import java.util.List;

/**
 * AccountService.
 */
public interface AccountService {
    Account create(Account account);

    Account mobileRegister(Account account);

    Account mobileRegisterExcel(Account account);

    Account update(Account account);

    Account get(Integer userId);

    Account getByMobile(String mobile);

    Account getByCompanyId(Integer companyId);

    Account getByWechat(Account account);

    Account getByAngelId(String angelId);

    void bindWechat(String openId,String unionId, String userName);

    void bindUnionId(String openId, String unionId);

    void bindAngel(String angelId, String userName);

    Account login(Account account);

    boolean checkMobile(String mobile);

    Results<Account> searchForPage(AccountOptions options);

    List<Account> search(AccountOptions options);

    Integer searchAddAccount(AccountOptions options);

    Integer searchAddAccountBySource(AccountOptions options);

    Integer searchDayLoginAccount(AccountOptions options);

    void changePassword(String mobile, String newPassword, String verifyCode);

    void resetPassword(String mobile, String newPassword, String verifyCode);

    Account getInvitationCode(String mobile);

    AccountBindRequest bindMobile(AccountBindRequest request);

    List<AccountViews> searchUserStatistics(String type);

    List<AccountViews> searchLoginUserStatistics(String type);

    void updateTelephoneSwitch(Integer type);
}
