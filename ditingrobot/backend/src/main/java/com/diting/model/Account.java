package com.diting.model;

import java.util.Date;

/**
 * Account
 */
public class Account extends BaseModel {
    //base info
    private String userName;
    private String password;
    private String mobile;
    private String realName;
    private String email;
    private String avatarUrl;//头像
    private String invitationCode;//邀请码
    private String verifyCode;//验证码
    private String ip;
    private Date lastLoginDate;//最后一次登录时间
    private Boolean forbiddenEnable; //false 不禁用 true 禁用
    private Boolean claimEnable;// true已认领 false 未认领
    private ChatStatistics chatStatistics; //聊天统计

    private String robotName;//机器人名称
    private String companyName;//公司名称
    private String newYearGreetings;//新年贺词
    private String uniqueId;

    private String angelId;  //天使号
    //wechat info
    private String openId;
    private String nickName;
    private String headImgUrl;//头像
    private String unionId;
    private String shortDomainName;
    private String robotVal;
    private Integer balance;
    private String sex;
    private Boolean telephoneSwitch;//电话开关

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    //company info
    private Integer companyId;

    private String source;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Boolean getForbiddenEnable() {
        return forbiddenEnable;
    }

    public void setForbiddenEnable(Boolean forbiddenEnable) {
        this.forbiddenEnable = forbiddenEnable;
    }

    public Boolean isClaimEnable() {
        return claimEnable;
    }

    public void setClaimEnable(Boolean claimEnable) {
        this.claimEnable = claimEnable;
    }

    public String getAngelId() {
        return angelId;
    }

    public void setAngelId(String angelId) {
        this.angelId = angelId;
    }

    public ChatStatistics getChatStatistics() {
        return chatStatistics;
    }

    public void setChatStatistics(ChatStatistics chatStatistics) {
        this.chatStatistics = chatStatistics;
    }

    public String getNewYearGreetings() {
        return newYearGreetings;
    }

    public void setNewYearGreetings(String newYearGreetings) {
        this.newYearGreetings = newYearGreetings;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getShortDomainName() {
        return shortDomainName;
    }

    public void setShortDomainName(String shortDomainName) {
        this.shortDomainName = shortDomainName;
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

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Boolean getTelephoneSwitch() {
        return telephoneSwitch;
    }

    public void setTelephoneSwitch(Boolean telephoneSwitch) {
        this.telephoneSwitch = telephoneSwitch;
    }
}
