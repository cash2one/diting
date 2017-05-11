package com.diting;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.testng.annotations.Test;

/**
 * TestSha1.
 */
public class TestSha1 extends BaseTest {

    @Test(enabled = false)
    public void testBVT() {
        String password = "1q2w3e4r";
        String userName = "sunhao";
        String password1 = new SimpleHash("SHA-1", userName, password).toString();
        System.out.println(password1);
    }
}
