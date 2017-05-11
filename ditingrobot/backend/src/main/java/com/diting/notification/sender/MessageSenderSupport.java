/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.notification.sender;

import com.diting.notification.message.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * MessageSenderSupport.
 */
public abstract class MessageSenderSupport implements MessageSender {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void send(NotificationMessage message) {

    }

}
