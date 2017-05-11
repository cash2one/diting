package com.diting;

import com.diting.cache.Cache;
import com.diting.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * TestCache.
 */
public class TestCache extends BaseTest {
    @Autowired
    @Qualifier("redisCache")
    private Cache redisCache;

    @Test(enabled = false)
    public void testBVT() {
        Account employee = new Account();
        employee.setUserName("sunhao");
        employee.setPassword("123456");
        employee.setRealName("孙昊");

        String key = System.currentTimeMillis() + "";

//        redisCache.put(key, employee,10);//十秒失效
        redisCache.put(key,employee);

        redisCache.put("Ooo","ooo");

        Account fetched = redisCache.get(key);
        Assert.assertNotNull(fetched);
    }
}
