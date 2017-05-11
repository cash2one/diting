/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.notification.notifier;


import com.diting.notification.message.NotificationMessage;

/**
 * PaymentNotifier.
 */
public class PaymentNotifier extends NotifierSupport {

    @Override
    public void notify(NotifierContext context) {
        String content = "您的余额已不足，为保障正常使用，请联系谛听客服进行缴费。（如已缴费，请忽略）";

        NotificationMessage message = new NotificationMessage();
        message.setMobile(context.getMobile());
        message.setContent(content);

        smsSender.send(message);
    }
}
