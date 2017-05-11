package com.diting.model;

/**
 * Created by Administrator on 2016/11/29.
 */
public class RegistrationUser {

    private Integer id;
    private String userName;//用户名
    private String nickname; //昵称
    private Integer allowCount;
    private Integer notCount;
    private Float sortScore;

    private String companyName;
    private String robotName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAllowCount() {
        return allowCount;
    }

    public void setAllowCount(Integer allowCount) {
        this.allowCount = allowCount;
    }

    public Integer getNotCount() {
        return notCount;
    }

    public void setNotCount(Integer notCount) {
        this.notCount = notCount;
    }

    public Float getSortScore() {
        return sortScore;
    }

    public void setSortScore(Float sortScore) {
        this.sortScore = sortScore;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }
}
