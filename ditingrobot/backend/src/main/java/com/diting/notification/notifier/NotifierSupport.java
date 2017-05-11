/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.notification.notifier;

import com.diting.cache.RedisImpl;
import com.diting.dao.AccountMapper;
import com.diting.dao.CompanyMapper;
import com.diting.notification.sender.MessageSender;
import com.diting.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * NotifierSupport.
 */
public abstract class NotifierSupport implements Notifier {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected CompanyMapper companyMapper;

    @Autowired
    protected AccountMapper accountMapper;

    @Autowired
    protected RedisImpl redis;

    @Autowired
    @Qualifier("jinLouSMSMessageSender")
    protected MessageSender smsSender;

    protected void safeRun(Callback callback) {
        try {
            callback.apply();
        } catch (Exception e) {
            LOGGER.error("Error occurred.", e);
        }
    }
}
