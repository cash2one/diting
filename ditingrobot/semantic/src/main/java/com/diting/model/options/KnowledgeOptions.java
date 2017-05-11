package com.diting.model.options;

/**
 * KnowledgeOptions
 */
public class KnowledgeOptions extends PageableOptions{

    private int companyId;//公司id

    private int accountId;//用户id

    private String keywords;  //根据关键字查询

    private int queryState;     //模糊与精准查询

    private int queryCriteria;   //排序条件

    private String uploadExcelPath;// excel上传路径

    private String startTime;

    private String endTime;

    private int type;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int conpany_id) {
        this.companyId = conpany_id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getQueryCriteria() {
        return queryCriteria;
    }

    public void setQueryCriteria(int queryCriteria) {
        this.queryCriteria = queryCriteria;
    }

    public int getQueryState() {
        return queryState;
    }

    public void setQueryState(int queryState) {
        this.queryState = queryState;
    }

    public String getUploadExcelPath() {
        return uploadExcelPath;
    }

    public void setUploadExcelPath(String uploadExcelPath) {
        this.uploadExcelPath = uploadExcelPath;
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

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
