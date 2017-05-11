/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Wallet.
 */
public class Mail extends BaseModel {
    private String biaoti;
    private String zhengwen;

    private List<Mail_phone> mail_phone;

    public List<Mail_phone> getMail_phone() {
        return mail_phone;
    }

    public void setMail_phone(List<Mail_phone> mail_phone) {
        this.mail_phone = mail_phone;
    }

    public String getBiaoti() {
        return biaoti;
    }

    public void setBiaoti(String biaoti) {
        this.biaoti = biaoti;
    }

    public String getZhengwen() {
        return zhengwen;
    }

    public void setZhengwen(String zhengwen) {
        this.zhengwen = zhengwen;
    }
}
