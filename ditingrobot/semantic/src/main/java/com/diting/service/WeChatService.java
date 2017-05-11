package com.diting.service;

import com.diting.model.Account;
import com.diting.model.WeChat;
import com.diting.model.WeChatAccount;
import com.diting.model.options.WeChatAccountOptions;
import com.diting.model.wechat.AcceptParams;
import com.diting.model.wechat.Authorization;

import java.util.List;
import java.util.Map;

/**
 * WeChatService.
 */
public interface WeChatService {
    WeChatAccount create(WeChatAccount weChatAccount);

    void delete(WeChatAccount weChatAccount);

    void getInfo(WeChat weChat) throws Exception;

    String authorize(Account account) throws Exception;

    void redirect(Authorization authorization) throws Exception;

    String accept(AcceptParams acceptParams) throws Exception;

    List<Map<String,Object>> searchAllCountByTime(WeChatAccountOptions options);
}
