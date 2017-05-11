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
public class Fans extends BaseModel {
    private String own_phone;
    private String oth_phone;
    private String userName;
    private String headImgUrl;
    private String cc;
    private String company_name;
    private String robot_name;
    private Integer fansNum;
    private String welcome;
    private BigDecimal robotValue;
    private Integer userId;
    private String attentionState;   //关注状态(是否关注机器人)
    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getRobot_name() {
        return robot_name;
    }

    public void setRobot_name(String robot_name) {
        this.robot_name = robot_name;
    }

    public String getOwn_phone() {
        return own_phone;
    }

    public void setOwn_phone(String own_phone) {
        this.own_phone = own_phone;
    }

    public String getOth_phone() {
        return oth_phone;
    }

    public void setOth_phone(String oth_phone) {
        this.oth_phone = oth_phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public BigDecimal getRobotValue() {
        return robotValue;
    }

    public void setRobotValue(BigDecimal robotValue) {
        this.robotValue = robotValue;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAttentionState() {
        return attentionState;
    }

    public void setAttentionState(String attentionState) {
        this.attentionState = attentionState;
    }
}
