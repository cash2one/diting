package com.diting.model;

/**
 * Robot
 */
public class Robot extends BaseModel {

    //base info
    private String name;
    private String welcomes;
    private String uniqueId;
    private String avatarUrl;
    private String token;
    private String shortDomainName;
    private Boolean enable; // false 启用 true 禁用
    private Boolean invokeEnable;
    private String profile;

    //invalid answer
    private String invalidAnswer1;
    private String invalidAnswer2;
    private String invalidAnswer3;
    private String invalidAnswer4;
    private String invalidAnswer5;
    private String invalidAnswer6;
    private String invalidAnswer7;
    private String invalidAnswer8;
    private String invalidAnswer9;
    private String invalidAnswer10;

    //account info
    private Integer accountId;

    //company info
    private Integer companyId;

    private String chargeMode;//收费模式

    private String robotVal;

    private Integer intel_calendar;

    private Integer industry;//行业
    private String companyName;//所有者名

    private String username;
    private String headImg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWelcomes() {
        return welcomes;
    }

    public void setWelcomes(String welcomes) {
        this.welcomes = welcomes;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Boolean isEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getInvalidAnswer1() {
        return invalidAnswer1;
    }

    public void setInvalidAnswer1(String invalidAnswer1) {
        this.invalidAnswer1 = invalidAnswer1;
    }

    public String getInvalidAnswer2() {
        return invalidAnswer2;
    }

    public void setInvalidAnswer2(String invalidAnswer2) {
        this.invalidAnswer2 = invalidAnswer2;
    }

    public String getInvalidAnswer3() {
        return invalidAnswer3;
    }

    public void setInvalidAnswer3(String invalidAnswer3) {
        this.invalidAnswer3 = invalidAnswer3;
    }

    public String getInvalidAnswer4() {
        return invalidAnswer4;
    }

    public void setInvalidAnswer4(String invalidAnswer4) {
        this.invalidAnswer4 = invalidAnswer4;
    }

    public String getInvalidAnswer5() {
        return invalidAnswer5;
    }

    public void setInvalidAnswer5(String invalidAnswer5) {
        this.invalidAnswer5 = invalidAnswer5;
    }

    public Boolean isInvokeEnable() {
        return invokeEnable;
    }

    public void setInvokeEnable(Boolean invokeEnable) {
        this.invokeEnable = invokeEnable;
    }

    public String getShortDomainName() {
        return shortDomainName;
    }

    public void setShortDomainName(String shortDomainName) {
        this.shortDomainName = shortDomainName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getInvalidAnswer6() {
        return invalidAnswer6;
    }

    public void setInvalidAnswer6(String invalidAnswer6) {
        this.invalidAnswer6 = invalidAnswer6;
    }

    public String getInvalidAnswer7() {
        return invalidAnswer7;
    }

    public void setInvalidAnswer7(String invalidAnswer7) {
        this.invalidAnswer7 = invalidAnswer7;
    }

    public String getInvalidAnswer8() {
        return invalidAnswer8;
    }

    public void setInvalidAnswer8(String invalidAnswer8) {
        this.invalidAnswer8 = invalidAnswer8;
    }

    public String getInvalidAnswer9() {
        return invalidAnswer9;
    }

    public void setInvalidAnswer9(String invalidAnswer9) {
        this.invalidAnswer9 = invalidAnswer9;
    }

    public String getInvalidAnswer10() {
        return invalidAnswer10;
    }

    public void setInvalidAnswer10(String invalidAnswer10) {
        this.invalidAnswer10 = invalidAnswer10;
    }

    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    public String getRobotVal() {
        return robotVal;
    }

    public void setRobotVal(String robotVal) {
        this.robotVal = robotVal;
    }

    public Integer getIntel_calendar() {
        return intel_calendar;
    }

    public void setIntel_calendar(Integer intel_calendar) {
        this.intel_calendar = intel_calendar;
    }

    public Integer getIndustry() {
        return industry;
    }

    public void setIndustry(Integer industry) {
        this.industry = industry;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}
