package com.diting.model;

import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;

/**
 * Company
 */
public class Company extends BaseModel {
    private String name;
    private String location;
    private String productName;
    private String logoUrl;
    private Integer industry;//行业
    private Integer chatLogAccount;
    private Integer chatLogYesterdayAccount;
    private String accuracyRate;
    private Integer residualFrequency;

    private String companyName;
    private String robotName;
    private String username;
    private String welcome;
    private String profile;
    private String robotVal;
    private BigDecimal robotValue;
    private Integer userId;
    private String headImgUrl;
    private String headPortrait;
    private Integer num;//粉丝数
    private String attentionState;
    private String source;//来源部门

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getIndustry() {
        return industry;
    }

    public void setIndustry(Integer industry) {
        this.industry = industry;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Transient
    private String uniqueId;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Integer getChatLogAccount() {
        return chatLogAccount;
    }

    public void setChatLogAccount(Integer chatLogAccount) {
        this.chatLogAccount = chatLogAccount;
    }

    public String getAccuracyRate() {
        return accuracyRate;
    }

    public void setAccuracyRate(String accuracyRate) {
        this.accuracyRate = accuracyRate;
    }

    public Integer getChatLogYesterdayAccount() {
        return chatLogYesterdayAccount;
    }

    public void setChatLogYesterdayAccount(Integer chatLogYesterdayAccount) {
        this.chatLogYesterdayAccount = chatLogYesterdayAccount;
    }

    public Integer getResidualFrequency() {
        return residualFrequency;
    }

    public void setResidualFrequency(Integer residualFrequency) {
        this.residualFrequency = residualFrequency;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRobotVal() {
        return robotVal;
    }

    public void setRobotVal(String robotVal) {
        this.robotVal = robotVal;
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

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getAttentionState() {
        return attentionState;
    }

    public void setAttentionState(String attentionState) {
        this.attentionState = attentionState;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
