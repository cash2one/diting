package com.diting.model;

/**
 * AccountLog
 */
public class AccountLog extends BaseModel {
    //base info
    private String content;
    private String reason;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
