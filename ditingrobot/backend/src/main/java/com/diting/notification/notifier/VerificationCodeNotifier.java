/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.notification.notifier;


import com.diting.notification.message.NotificationMessage;

/**
 * VerificationCodeNotifier.
 */
public class VerificationCodeNotifier extends NotifierSupport {

    @Override
    public void notify(NotifierContext context) {
        String content = "验证码：[" + context.getVerificationCode() + "]，打死也不能告诉别人哦。如非本人操作，请忽略。";

        NotificationMessage message = new NotificationMessage();
        message.setMobile(context.getMobile());
        message.setContent(content);

        smsSender.send(message);
    }
}
