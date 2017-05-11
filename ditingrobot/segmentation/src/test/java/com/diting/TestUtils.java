package com.diting;

import com.diting.util.Utils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.testng.annotations.Test;

/**
 * TestUtils.
 */
public class TestUtils extends BaseTest {

    @Test(enabled = false)
    public void testBVT() {
        String password = "1q2w3e4r";
        String mobile = "18612172117";
        password = Utils.sha1(password);
        String password1 = new SimpleHash("SHA-1", mobile, password).toString();
        System.out.println(password1);
    }

}
