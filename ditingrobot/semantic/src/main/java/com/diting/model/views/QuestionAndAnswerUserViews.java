package com.diting.model.views;

/**
 * Created by Administrator on 2017/5/8.
 */
public class QuestionAndAnswerUserViews {
    private String time;
    private Integer questionUserNum;//提问用户数
    private Integer answerUserNum;//回答用户数

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getQuestionUserNum() {
        return questionUserNum;
    }

    public void setQuestionUserNum(Integer questionUserNum) {
        this.questionUserNum = questionUserNum;
    }

    public Integer getAnswerUserNum() {
        return answerUserNum;
    }

    public void setAnswerUserNum(Integer answerUserNum) {
        this.answerUserNum = answerUserNum;
    }
}
