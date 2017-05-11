package com.diting.model;

import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * Account
 */
public class StaAccount extends BaseModel {
    //base info
    private Date dataTime;
    private Integer dayAccountCount;
    private Integer dayAccountCountOne;
    private Integer dayAccountCountTwo;
    private Integer dayAccountCountThree;
    private Integer dayAccountCountFour;
    private Integer dayAccountCountFive;
    private Integer dayLoginCount;
    private Integer dayValidCount;
    private Integer dayInvalidCount;
    private Integer dayQuestionCount;
    private Integer dayKnowledgeCount;
    private Integer dayQuestionAndAnswerNumber;
    private Integer dayQuestionAndAnswerUserNum;//单日B端问答用户数,单日产生对话的机器人数量
    @Transient
    private Integer allAccountCount;
    @Transient
    private Integer allValidCount;
    @Transient
    private Integer allInvalidCount;
    @Transient
    private Integer allQuestionCount;
    @Transient
    private Integer allKnowledgeCount;

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public Integer getDayAccountCount() {
        return dayAccountCount;
    }

    public void setDayAccountCount(Integer dayAccountCount) {
        this.dayAccountCount = dayAccountCount;
    }

    public Integer getDayLoginCount() {
        return dayLoginCount;
    }

    public void setDayLoginCount(Integer dayLoginCount) {
        this.dayLoginCount = dayLoginCount;
    }

    public Integer getDayValidCount() {
        return dayValidCount;
    }

    public void setDayValidCount(Integer dayValidCount) {
        this.dayValidCount = dayValidCount;
    }

    public Integer getDayInvalidCount() {
        return dayInvalidCount;
    }

    public void setDayInvalidCount(Integer dayInvalidCount) {
        this.dayInvalidCount = dayInvalidCount;
    }

    public Integer getDayQuestionCount() {
        return dayQuestionCount;
    }

    public void setDayQuestionCount(Integer dayQuestionCount) {
        this.dayQuestionCount = dayQuestionCount;
    }

    public Integer getDayKnowledgeCount() {
        return dayKnowledgeCount;
    }

    public void setDayKnowledgeCount(Integer dayKnowledgeCount) {
        this.dayKnowledgeCount = dayKnowledgeCount;
    }

    public Integer getAllAccountCount() {
        return allAccountCount;
    }

    public void setAllAccountCount(Integer allAccountCount) {
        this.allAccountCount = allAccountCount;
    }

    public Integer getAllValidCount() {
        return allValidCount;
    }

    public void setAllValidCount(Integer allValidCount) {
        this.allValidCount = allValidCount;
    }


    public Integer getAllInvalidCount() {
        return allInvalidCount;
    }

    public void setAllInvalidCount(Integer allInvalidCount) {
        this.allInvalidCount = allInvalidCount;
    }



    public Integer getAllQuestionCount() {
        return allQuestionCount;
    }

    public void setAllQuestionCount(Integer allQuestionCount) {
        this.allQuestionCount = allQuestionCount;
    }


    public Integer getAllKnowledgeCount() {
        return allKnowledgeCount;
    }

    public void setAllKnowledgeCount(Integer allKnowledgeCount) {
        this.allKnowledgeCount = allKnowledgeCount;
    }

    public Integer getDayQuestionAndAnswerNumber() {
        return dayQuestionAndAnswerNumber;
    }

    public void setDayQuestionAndAnswerNumber(Integer dayQuestionAndAnswerNumber) {
        this.dayQuestionAndAnswerNumber = dayQuestionAndAnswerNumber;
    }

    public Integer getDayQuestionAndAnswerUserNum() {
        return dayQuestionAndAnswerUserNum;
    }

    public void setDayQuestionAndAnswerUserNum(Integer dayQuestionAndAnswerUserNum) {
        this.dayQuestionAndAnswerUserNum = dayQuestionAndAnswerUserNum;
    }

    public Integer getDayAccountCountOne() {
        return dayAccountCountOne;
    }

    public void setDayAccountCountOne(Integer dayAccountCountOne) {
        this.dayAccountCountOne = dayAccountCountOne;
    }

    public Integer getDayAccountCountTwo() {
        return dayAccountCountTwo;
    }

    public void setDayAccountCountTwo(Integer dayAccountCountTwo) {
        this.dayAccountCountTwo = dayAccountCountTwo;
    }

    public Integer getDayAccountCountThree() {
        return dayAccountCountThree;
    }

    public void setDayAccountCountThree(Integer dayAccountCountThree) {
        this.dayAccountCountThree = dayAccountCountThree;
    }

    public Integer getDayAccountCountFour() {
        return dayAccountCountFour;
    }

    public void setDayAccountCountFour(Integer dayAccountCountFour) {
        this.dayAccountCountFour = dayAccountCountFour;
    }

    public Integer getDayAccountCountFive() {
        return dayAccountCountFive;
    }

    public void setDayAccountCountFive(Integer dayAccountCountFive) {
        this.dayAccountCountFive = dayAccountCountFive;
    }
}
