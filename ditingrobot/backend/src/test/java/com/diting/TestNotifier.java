package com.diting;

import com.diting.notification.notifier.NotifierContext;
import com.diting.notification.notifier.VerificationCodeNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * TestCache.
 */
public class TestNotifier extends BaseTest {
    @Autowired
    private VerificationCodeNotifier notifier;

    @Test(enabled = false)
    public void testBVT() {
        NotifierContext context = new NotifierContext();
        context.setMobile("18612172117");
        context.setVerificationCode("123456");
        notifier.notify(context);
    }
}
