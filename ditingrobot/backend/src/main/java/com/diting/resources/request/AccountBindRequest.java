/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.resources.request;

/**
 * AccountBindRequest.
 */
public class AccountBindRequest {
    private Integer robotId;
    //account
    private Integer accountId;
    private String userName;
    private String password;
    private String mobile;
    private Boolean claimEnable;
    private String invitationCode;
    private String newYearGreetings;

    //company
    private Integer companyId;
    private String companyCreatedBy;

    //invalid_question
    private String oldMobile;

    private String source;

    public Integer getRobotId() {
        return robotId;
    }

    public void setRobotId(Integer robotId) {
        this.robotId = robotId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isClaimEnable() {
        return claimEnable;
    }

    public void setClaimEnable(Boolean claimEnable) {
        this.claimEnable = claimEnable;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCreatedBy() {
        return companyCreatedBy;
    }

    public void setCompanyCreatedBy(String companyCreatedBy) {
        this.companyCreatedBy = companyCreatedBy;
    }

    public String getOldMobile() {
        return oldMobile;
    }

    public void setOldMobile(String oldMobile) {
        this.oldMobile = oldMobile;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNewYearGreetings() {
        return newYearGreetings;
    }

    public void setNewYearGreetings(String newYearGreetings) {
        this.newYearGreetings = newYearGreetings;
    }
}
