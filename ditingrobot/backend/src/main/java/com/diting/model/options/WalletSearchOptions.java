/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.model.options;

/**
 * WalletSearchOptions.
 */
public class WalletSearchOptions extends PageableOptions {
    private String userId;
    private String userType;
    private String type;
    private String legerType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLegerType() {
        return legerType;
    }

    public void setLegerType(String legerType) {
        this.legerType = legerType;
    }
}
