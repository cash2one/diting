package com.diting.model.views;

/**
 * Created by Administrator on 2017/2/16.
 */
public class AppInfo {
    private String  robotName;
    private String  CompanyName;
    private Integer fansCount;
    private String headPortrait;
    private String unique_id;
    private String short_domain_name;

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getShort_domain_name() {
        return short_domain_name;
    }

    public void setShort_domain_name(String short_domain_name) {
        this.short_domain_name = short_domain_name;
    }
}
