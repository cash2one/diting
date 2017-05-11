package com.diting.model;

import java.util.List;

/**
 * Created by liufei on 2016/10/11.
 */
public class Message extends BaseModel{

    private Integer companyId;
    private Integer userId;
    private String suggestion;//意见
    private String contactInformation;//联系方式
    private String feedback;
    private List<FeedbackMessage> feedbackMessageList;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public List<FeedbackMessage> getFeedbackMessageList() {
        return feedbackMessageList;
    }

    public void setFeedbackMessageList(List<FeedbackMessage> feedbackMessageList) {
        this.feedbackMessageList = feedbackMessageList;
    }
}
