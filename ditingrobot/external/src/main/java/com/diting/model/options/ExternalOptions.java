package com.diting.model.options;

import java.util.List;

/**
 * ExternalOptions
 */
public class ExternalOptions extends PageableOptions {
    private String url;
    private String scene;
    private String question;
    private String lastQuestion;
    private String thisScene;
    private String lastScene;
    private String kw1;
    private String kw2;
    private String kw3;
    private String kw4;
    private String kw5;
    private String uuid;
    private List<Integer> openIds;
    private List<Integer> closedIds;
    private Integer userId;
    private String robotName;
    private String source;

    private String uniqueId;
    private String ownUniqueId;
    private Integer intel_calendar;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getKw1() {
        return kw1;
    }

    public void setKw1(String kw1) {
        this.kw1 = kw1;
    }

    public String getKw2() {
        return kw2;
    }

    public void setKw2(String kw2) {
        this.kw2 = kw2;
    }

    public String getKw3() {
        return kw3;
    }

    public void setKw3(String kw3) {
        this.kw3 = kw3;
    }

    public String getKw4() {
        return kw4;
    }

    public void setKw4(String kw4) {
        this.kw4 = kw4;
    }

    public String getKw5() {
        return kw5;
    }

    public void setKw5(String kw5) {
        this.kw5 = kw5;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLastQuestion() {
        return lastQuestion;
    }

    public void setLastQuestion(String lastQuestion) {
        this.lastQuestion = lastQuestion;
    }

    public List<Integer> getOpenIds() {
        return openIds;
    }

    public void setOpenIds(List<Integer> openIds) {
        this.openIds = openIds;
    }

    public List<Integer> getClosedIds() {
        return closedIds;
    }

    public void setClosedIds(List<Integer> closedIds) {
        this.closedIds = closedIds;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getOwnUniqueId() {
        return ownUniqueId;
    }

    public void setOwnUniqueId(String ownUniqueId) {
        this.ownUniqueId = ownUniqueId;
    }

    public Integer getIntel_calendar() {
        return intel_calendar;
    }

    public void setIntel_calendar(Integer intel_calendar) {
        this.intel_calendar = intel_calendar;
    }

    public String getThisScene() {
        return thisScene;
    }

    public void setThisScene(String thisScene) {
        this.thisScene = thisScene;
    }

    public String getLastScene() {
        return lastScene;
    }

    public void setLastScene(String lastScene) {
        this.lastScene = lastScene;
    }
}
