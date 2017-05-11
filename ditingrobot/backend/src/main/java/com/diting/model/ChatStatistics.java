package com.diting.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liufei on 2016/12/20.
 */
public class ChatStatistics {
    private Integer id;
    private String time;
    private Integer userId;
    private Integer companyId;
    private Integer yesterdayCount;
    private Integer allCount;
    private String accuracyRate;
    private BigDecimal robotValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAccuracyRate() {
        return accuracyRate;
    }

    public void setAccuracyRate(String accuracyRate) {
        this.accuracyRate = accuracyRate;
    }

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getYesterdayCount() {
        return yesterdayCount;
    }

    public void setYesterdayCount(Integer yesterdayCount) {
        this.yesterdayCount = yesterdayCount;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public BigDecimal getRobotValue() {
        return robotValue;
    }

    public void setRobotValue(BigDecimal robotValue) {
        this.robotValue = robotValue;
    }
}
