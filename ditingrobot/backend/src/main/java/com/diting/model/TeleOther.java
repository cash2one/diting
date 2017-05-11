package com.diting.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shenkun on 2017/2/20.
 */
public class TeleOther implements Serializable{
    private Integer id;
    private String createdBy;
    private Date createdTime;
    private String forword_name;//发信人机器人名
    private String receive_name;//接收人机器人名
    private String message;//消息
    private String flag;//是否已读 0未读 1已读
    private String forword_unique_id;//发信人机器人唯一标示

    public String getForword_unique_id() {
        return forword_unique_id;
    }

    public void setForword_unique_id(String forword_unique_id) {
        this.forword_unique_id = forword_unique_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getForword_name() {
        return forword_name;
    }

    public void setForword_name(String forword_name) {
        this.forword_name = forword_name;
    }

    public String getReceive_name() {
        return receive_name;
    }

    public void setReceive_name(String receive_name) {
        this.receive_name = receive_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
