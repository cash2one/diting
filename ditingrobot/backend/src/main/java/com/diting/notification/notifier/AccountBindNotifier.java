/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.notification.notifier;


import com.diting.notification.message.NotificationMessage;

/**
 * AccountBindNotifier.
 */
public class AccountBindNotifier extends NotifierSupport {

    @Override
    public void notify(NotifierContext context) {
        String content = "您的用户名为手机号，密码（区分大小写）：" + context.getPassword() + "。请在这里登录http://www.ditingai.com/login（如非本人操作，请忽略本条短信）";

        NotificationMessage message = new NotificationMessage();
        message.setMobile(context.getMobile());
        message.setContent(content);

        smsSender.send(message);
    }
}
