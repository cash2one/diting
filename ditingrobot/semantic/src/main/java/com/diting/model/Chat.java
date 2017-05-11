package com.diting.model;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liufei on 2016/8/4.
 */
public class Chat implements Serializable{

    private static final long serialVersionUID = 1L;

    private String uuid;  //用户身份
    private String question; //问题
    private String answer; //答案
    private String scene; //场景
    private String qieci; //切词
    private String wendabuquan; // 问答补全
    private String enable; //是否禁用A库 false启用 true禁用
    private Boolean invokeEnable;//是否禁用应用
    private String available; //是否有效
    private List<String> invalids; //自定义无效问题
    private String fromusername;//微信用户来源
    private String username; //登录账号
    private Integer accountId;//用户ID
    private String keywords;//关键字
    private String robotName;//机器人名称
    private Integer companyId;
    private String companyName;//公司名称
    private String unique_id;//机器人id
    private String own_unique_id;//登录用户机器人ID
    private String answer1;//无效问答
    private String answer2;//无效问答
    private String answer3;//无效问答
    private String answer4;//无效问答
    private String answer5;//无效问答
    private String actionOption;//动作指令
    private Boolean forbiddenEnable; //false 不禁用 true 禁用
    private String source;//来源，微信、web...
    private String img_url;
    private Integer intel_calendar;
    private String charge_mode;
    private List<ChangeBook> changeBooks;
    private String skip_url;//变身跳转url
    private String welcome;//欢迎语
    private String headImgUrl;//机器人头像
    private String profile;//机器人简介
    private String mp3Url;//音乐播放链接
    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getSkip_url() {
        return skip_url;
    }

    public void setSkip_url(String skip_url) {
        this.skip_url = skip_url;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getQieci() {
        return qieci;
    }

    public void setQieci(String qieci) {
        this.qieci = qieci;
    }

    public String getWendabuquan() {
        return wendabuquan;
    }

    public void setWendabuquan(String wendabuquan) {
        this.wendabuquan = wendabuquan;
    }

    public List<String> getInvalids() {
        return invalids;
    }

    public void setInvalids(List<String> invalids) {
        this.invalids = invalids;
    }

    public String getFromusername() {
        return fromusername;
    }

    public void setFromusername(String fromusername) {
        this.fromusername = fromusername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }


    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getAnswer5() {
        return answer5;
    }

    public void setAnswer5(String answer5) {
        this.answer5 = answer5;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Boolean isInvokeEnable() {
        return invokeEnable;
    }

    public void setInvokeEnable(Boolean invokeEnable) {
        this.invokeEnable = invokeEnable;
    }

    public String getActionOption() {
        return actionOption;
    }

    public void setActionOption(String actionOption) {
        this.actionOption = actionOption;
    }

    public Boolean getForbiddenEnable() {
        return forbiddenEnable;
    }

    public void setForbiddenEnable(Boolean forbiddenEnable) {
        this.forbiddenEnable = forbiddenEnable;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getOwn_unique_id() {
        return own_unique_id;
    }

    public void setOwn_unique_id(String own_unique_id) {
        this.own_unique_id = own_unique_id;
    }

    public Integer getIntel_calendar() {
        return intel_calendar;
    }

    public void setIntel_calendar(Integer intel_calendar) {
        this.intel_calendar = intel_calendar;
    }

    public String getCharge_mode() {
        return charge_mode;
    }

    public void setCharge_mode(String charge_mode) {
        this.charge_mode = charge_mode;
    }

    public List<ChangeBook> getChangeBooks() {
        return changeBooks;
    }

    public void setChangeBooks(List<ChangeBook> changeBooks) {
        this.changeBooks = changeBooks;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }
}
