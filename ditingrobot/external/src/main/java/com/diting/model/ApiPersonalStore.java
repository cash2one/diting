package com.diting.model;


/**
 * Created by Administrator on 2017/3/7.
 */
public class ApiPersonalStore extends BaseModel {
    private Integer userId;
    private String providerName;
    private String providerEnName;
    private String mobile;
    private String email;
    private String serverName;
    private String serverEnName;
    private String scene;
    private String description;
    private String name;
    private String enName;
    private String url;
    private String version;
    private String method;
    private Integer approvalEnable;//审批标识 0 审批不通过  1 审批通过
    private Integer enable;//启用标识 0 停止  1 开启
    private String keywords;
    private String address;
    private String ids;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderEnName() {
        return providerEnName;
    }

    public void setProviderEnName(String providerEnName) {
        this.providerEnName = providerEnName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerEnName() {
        return serverEnName;
    }

    public void setServerEnName(String serverEnName) {
        this.serverEnName = serverEnName;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getApprovalEnable() {
        return approvalEnable;
    }

    public void setApprovalEnable(Integer approvalEnable) {
        this.approvalEnable = approvalEnable;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
