package com.diting.model;

/**
 * Created by Administrator on 2017/4/12.
 */
public class Home {
    private Integer yesterdayVisitorsNum;//昨日访客数量
    private Integer yesterdayAskNum;//昨日问答数量
    private Integer yesterdayUnknownQuestionNum;//昨日未知问题
    private Integer yesterdayNewKnowledgeNum;//昨日新增知识

    public Integer getYesterdayVisitorsNum() {
        return yesterdayVisitorsNum;
    }

    public void setYesterdayVisitorsNum(Integer yesterdayVisitorsNum) {
        this.yesterdayVisitorsNum = yesterdayVisitorsNum;
    }

    public Integer getYesterdayAskNum() {
        return yesterdayAskNum;
    }

    public void setYesterdayAskNum(Integer yesterdayAskNum) {
        this.yesterdayAskNum = yesterdayAskNum;
    }

    public Integer getYesterdayUnknownQuestionNum() {
        return yesterdayUnknownQuestionNum;
    }

    public void setYesterdayUnknownQuestionNum(Integer yesterdayUnknownQuestionNum) {
        this.yesterdayUnknownQuestionNum = yesterdayUnknownQuestionNum;
    }

    public Integer getYesterdayNewKnowledgeNum() {
        return yesterdayNewKnowledgeNum;
    }

    public void setYesterdayNewKnowledgeNum(Integer yesterdayNewKnowledgeNum) {
        this.yesterdayNewKnowledgeNum = yesterdayNewKnowledgeNum;
    }
}
