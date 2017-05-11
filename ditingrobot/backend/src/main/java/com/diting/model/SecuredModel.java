/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.model;

/**
 * SecuredModel.
 */
public class SecuredModel extends BaseModel {
    private Long salt;
    private String sign;

    public Long getSalt() {
        return salt;
    }

    public void setSalt(Long salt) {
        this.salt = salt;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
