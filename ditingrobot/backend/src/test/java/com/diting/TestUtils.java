package com.diting;

import com.diting.util.RC4Utils;
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

    @Test(enabled = false)
    public void testBVT1() {
        String key = "54sdaf456rwe";

//            System.out.println(RC4Util.encry_RC4_string(
//                    "{\"header\":{\"appid\":\"123\",\"sid\":\"12315101114235600078\",\"cmd\":\"00000001\"}}", key));
//
//            System.out.println(RC4Util.decry_RC4(
//                    "71881d480db384878e16d5bb822f59ee6a723aa8590627f7f47d688e2de636a6f466b412fade06226068ca10de0352055631b93868d160b2b7bd10c83741a4f5f4528c4533c0657d",
//                    key));

        try {
            System.out.println(RC4Utils.encry_RC4_string(String.format("%07d", 1), "18518550624").toUpperCase());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
