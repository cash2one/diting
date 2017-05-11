/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 SilkCloud and/or its affiliates. All rights reserved.
 */
package com.diting.core;

/**
 * Universe.
 */
public class Universe {
    private static ThreadLocal<Universe> current = new ThreadLocal<Universe>();

    private boolean countingMode = false;

    private String userName;

    private Integer userId;

    private Integer userType;

    private String referrer;

    private String ip;

    public static Universe current() {
        Universe universe = current.get();
        if (universe == null) {
            universe = new Universe();
            current.set(universe);
        }

        return universe;
    }

    public static void clear() {
        current.set(null);
    }

    public boolean isCountingMode() {
        return countingMode;
    }

    public void setCountingMode(boolean countingMode) {
        this.countingMode = countingMode;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
