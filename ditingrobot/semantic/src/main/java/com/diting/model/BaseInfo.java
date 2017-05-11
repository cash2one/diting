package com.diting.model;

import java.util.List;

/**
 * Created by liufei on 2016/9/27.
 */
public class BaseInfo extends BaseModel{

    private String companyName;//公司名称
    private String companyUrl;//公司网址
    private String companyAddress;//公司地址
    private String busLine;//公交路线
    private String companyTel;//公司电话
    private String companyBusiness;//公司业务
    private String companyWeChat;//公司微信
    private String companySize;//公司规模
    private String companyProfile;//公司简介
    private String productIntroduction;//产品介绍
    private String technologyAdvantage;//技术优势
    private String industryWhere;//所在行业
    private String developmentProspect;//发展前景
    private String corporateCulture;//企业文化
    private String workShift;//上班时间
    private String companyLeadership;//公司领导
    private Integer accountId;
    private Integer companyId;
    private String financingSituation;//融资状况
    private String hotPositions;//热招职位
    private List<Recruit> recruitList;//招聘模板信息

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getBusLine() {
        return busLine;
    }

    public void setBusLine(String busLine) {
        this.busLine = busLine;
    }

    public String getCompanyTel() {
        return companyTel;
    }

    public void setCompanyTel(String companyTel) {
        this.companyTel = companyTel;
    }

    public String getCompanyBusiness() {
        return companyBusiness;
    }

    public void setCompanyBusiness(String companyBusiness) {
        this.companyBusiness = companyBusiness;
    }

    public String getCompanyLeadership() {
        return companyLeadership;
    }

    public void setCompanyLeadership(String companyLeadership) {
        this.companyLeadership = companyLeadership;
    }

    public String getCompanyProfile() {
        return companyProfile;
    }

    public void setCompanyProfile(String companyProfile) {
        this.companyProfile = companyProfile;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public String getCompanyWeChat() {
        return companyWeChat;
    }

    public void setCompanyWeChat(String companyWeChat) {
        this.companyWeChat = companyWeChat;
    }

    public String getCorporateCulture() {
        return corporateCulture;
    }

    public void setCorporateCulture(String corporateCulture) {
        this.corporateCulture = corporateCulture;
    }

    public String getDevelopmentProspect() {
        return developmentProspect;
    }

    public void setDevelopmentProspect(String developmentProspect) {
        this.developmentProspect = developmentProspect;
    }

    public String getIndustryWhere() {
        return industryWhere;
    }

    public void setIndustryWhere(String industryWhere) {
        this.industryWhere = industryWhere;
    }

    public String getProductIntroduction() {
        return productIntroduction;
    }

    public void setProductIntroduction(String productIntroduction) {
        this.productIntroduction = productIntroduction;
    }

    public String getTechnologyAdvantage() {
        return technologyAdvantage;
    }

    public void setTechnologyAdvantage(String technologyAdvantage) {
        this.technologyAdvantage = technologyAdvantage;
    }

    public String getWorkShift() {
        return workShift;
    }

    public void setWorkShift(String workShift) {
        this.workShift = workShift;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getFinancingSituation() {
        return financingSituation;
    }

    public void setFinancingSituation(String financingSituation) {
        this.financingSituation = financingSituation;
    }

    public String getHotPositions() {
        return hotPositions;
    }

    public void setHotPositions(String hotPositions) {
        this.hotPositions = hotPositions;
    }

    public List<Recruit> getRecruitList() {
        return recruitList;
    }

    public void setRecruitList(List<Recruit> recruitList) {
        this.recruitList = recruitList;
    }
}
