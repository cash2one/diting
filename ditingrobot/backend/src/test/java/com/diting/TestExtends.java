package com.diting;

import com.alibaba.fastjson.JSON;
import com.diting.model.Account;
import com.diting.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created by Thinkpad on 2016/6/24.
 */
public class TestExtends extends BaseTest {
    @Autowired
    private AccountService accountService;

    @Test(enabled = false)
    public void testBVT() {
        Account account = accountService.get(1);
        // System.out.println(user.getUserName());
        // logger.info("值："+user.getUserName());
        logger.info(JSON.toJSONString(account));

    }
}
