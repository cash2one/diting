package com.diting.model;

/**
 * Created by Administrator on 2017/3/10.
 */
public class BrushSheetInfo extends BaseModel{

    private String ip;
    private String deviceName;
    private String formFactory;
    private String is_mobile;
    private String brandName;
    private String modelName;
    private String devicesOs;
    private String devicesOsVersion;
    private String uuid;
    private Integer userId;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getFormFactory() {
        return formFactory;
    }

    public void setFormFactory(String formFactory) {
        this.formFactory = formFactory;
    }

    public String getIs_mobile() {
        return is_mobile;
    }

    public void setIs_mobile(String is_mobile) {
        this.is_mobile = is_mobile;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDevicesOs() {
        return devicesOs;
    }

    public void setDevicesOs(String devicesOs) {
        this.devicesOs = devicesOs;
    }

    public String getDevicesOsVersion() {
        return devicesOsVersion;
    }

    public void setDevicesOsVersion(String devicesOsVersion) {
        this.devicesOsVersion = devicesOsVersion;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
