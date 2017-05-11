package com.diting.dao;

import com.diting.model.Account;
import com.diting.model.options.AccountOptions;
import com.diting.model.options.CompanyOptions;
import com.diting.resources.request.AccountBindRequest;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * AccountMapper.
 */
public interface AccountMapper {

    void create(Account account);

    void update(Account account);

    void updateLastLoginTime(@Param("userId") Integer userId);

    void updatePassword(@Param("userId") Integer userId, @Param("password") String password);

    void updateOpenId(@Param("openId") String openId, @Param("unionId") String unionId,@Param("userName") String userName);

    void updateUnionId(@Param("openId") String openId, @Param("unionId") String unionId);

    void updateAngelId(@Param("angelId") String angelId, @Param("userName") String userName);

    void updateRecharge(@Param("userId") Integer userId);

    boolean checkMobileExists(@Param("mobile") String mobile);

    boolean checkPassword(@Param("userId") Integer userId, @Param("password") String password);

    Account get(@Param("userId") Integer userId);

    Account getByMobile(@Param("mobile") String mobile);

    Account getByUsername(@Param("username") String username);

    Account getByOpenId(@Param("openId") String openId);

    Account getByUnionId(@Param("UnionId") String UnionId);

    Account getByAngelId(@Param("angelId") String angelId);

    Account getByCompanyId(@Param("companyId") Integer companyId);

    Account checkUsernameLogin(@Param("userName")String userName, @Param("password")String password);

    List<Account> searchForPage(AccountOptions options);

    List<Account> searchAgentForPage(CompanyOptions options);

    List<Account> searchAccountForPage(CompanyOptions options);

    List<Account> searchForPageBytime(CompanyOptions options);

    List<Account> searchUserStatistics(@Param("startTime") Date startTime,@Param("endTime") Date endTime);

    List<Account> searchLoginUserStatistics(@Param("startTime") Date startTime,@Param("endTime") Date endTime);

    List<Account> searchAccountByWhereForPage(CompanyOptions options);

    List<Account> search(AccountOptions options);

    Integer searchAddAccount(AccountOptions options);

    Integer searchAddAccountBySource(AccountOptions options);

    Integer searchDayLoginAccount(AccountOptions options);

    void bindMobile(AccountBindRequest request);

    void updateTelephoneSwitch(@Param("type") Integer type,@Param("accountId") Integer accountId);
}
