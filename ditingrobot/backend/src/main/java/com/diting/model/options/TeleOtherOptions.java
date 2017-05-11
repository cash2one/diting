package com.diting.model.options;

/**
 * TeleOtherOptions
 */
public class TeleOtherOptions extends PageableOptions{

    private Integer id;
    private String forword_name;
    private String receive_name;
    private String message;
    private String flag;
    private String forword_unique_id;
    private String startTime;
    private String endTime;

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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
