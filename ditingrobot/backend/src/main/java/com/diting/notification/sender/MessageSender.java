/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.notification.sender;

import com.diting.notification.message.NotificationMessage;

/**
 * MessageSender.
 */
public interface MessageSender {
    void send(NotificationMessage message);
}
