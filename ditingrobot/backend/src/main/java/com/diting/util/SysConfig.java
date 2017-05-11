/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 SilkCloud and/or its affiliates. All rights reserved.
 */
package com.diting.util;

import org.springframework.beans.factory.annotation.Value;

/**
 * SysConfig.
 */
public class SysConfig {
    private static String appKey;

    private static String ipWhiteList;

    public static String getAppKey() {
        return appKey;
    }

    @Value("${sys.appKey}")
    public void setAppKey(String appKey) {
        SysConfig.appKey = appKey;
    }

    public static String getIpWhiteList() {
        return ipWhiteList;
    }

    @Value("${sys.ipWhiteList}")
    public void setIpWhiteList(String ipWhiteList) {
        SysConfig.ipWhiteList = ipWhiteList;
    }
}
