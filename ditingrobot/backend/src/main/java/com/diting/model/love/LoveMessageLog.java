package com.diting.model.love;

import java.util.Date;

/**
 * Created by shenkun on 2017/3/10.
 */
public class LoveMessageLog {
    private Integer id ;
    private Date createdTime;
    private String forwordName;
    private Integer forwordId;
    private String receiveName;
    private Integer receiveId;
    private String message;
    private Integer loveId;
    private Integer flag;
    private Integer messageType;
    private String receiveHeadImgUrl;

    public String getReceiveHeadImgUrl() {
        return receiveHeadImgUrl;
    }

    public void setReceiveHeadImgUrl(String receiveHeadImgUrl) {
        this.receiveHeadImgUrl = receiveHeadImgUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getForwordName() {
        return forwordName;
    }

    public void setForwordName(String forwordName) {
        this.forwordName = forwordName;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public Integer getForwordId() {
        return forwordId;
    }

    public void setForwordId(Integer forwordId) {
        this.forwordId = forwordId;
    }

    public Integer getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Integer receiveId) {
        this.receiveId = receiveId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getLoveId() {
        return loveId;
    }

    public void setLoveId(Integer loveId) {
        this.loveId = loveId;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }
}
