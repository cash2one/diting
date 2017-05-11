package com.diting.model.views;

/**
 * Created by Administrator on 2017/5/4.
 */
public class ChatLogViews {
    private String time;
    private Integer effectiveQuestionNum;
    private Integer invalidQuestionNum;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getEffectiveQuestionNum() {
        return effectiveQuestionNum;
    }

    public void setEffectiveQuestionNum(Integer effectiveQuestionNum) {
        this.effectiveQuestionNum = effectiveQuestionNum;
    }

    public Integer getInvalidQuestionNum() {
        return invalidQuestionNum;
    }

    public void setInvalidQuestionNum(Integer invalidQuestionNum) {
        this.invalidQuestionNum = invalidQuestionNum;
    }
}
