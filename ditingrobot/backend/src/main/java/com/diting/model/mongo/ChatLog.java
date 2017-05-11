package com.diting.model.mongo;


import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;


/**
 * 聊天日志
 * Created by liufei on 2016/8/11.
 */
@Document(collection = "chatlog")
public class ChatLog extends BaseMongoModel {

    private Integer userId;
    private String uuid;//游客id
    private String question;
    private String answer;
    private String scene;
    private String username;
    private String companyName;
    private String robotName;
    private String app_key;
    private String ip;
    private String extra1;//是否是有效问答，0表示有效
    private String extra2;
    private String extra3;
    private String extra4;//来源,1表示pc端,2表示微信
    private Integer num;
    private Integer count;
    private List<String> questions;
    private String welcome;
    private String profile;
    private String oldUsername;
    private String newUsername;
    private BigDecimal robotValue;
    private String charge_mode;
    private String dataSource;//数据来源A库or B库
    private String loginUsername;
    private String headImgUrl;
    private String attentionState;//关注状态
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }


    public String getExtra1() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public String getExtra3() {
        return extra3;
    }

    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }

    public String getExtra4() {
        return extra4;
    }

    public void setExtra4(String extra4) {
        this.extra4 = extra4;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
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

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getOldUsername() {
        return oldUsername;
    }

    public void setOldUsername(String oldUsername) {
        this.oldUsername = oldUsername;
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

    public String getCharge_mode() {
        return charge_mode;
    }

    public void setCharge_mode(String charge_mode) {
        this.charge_mode = charge_mode;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getAttentionState() {
        return attentionState;
    }

    public void setAttentionState(String attentionState) {
        this.attentionState = attentionState;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
