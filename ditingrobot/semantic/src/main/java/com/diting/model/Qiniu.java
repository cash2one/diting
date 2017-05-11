package com.diting.model;

/**
 * Created by Administrator on 2017/1/21.
 */
public class Qiniu {
    private String img_url;
    private String message;
    private String upload_url;
    private String rnd_name;
    private String bucketName;
    private String bucket_domail;
    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUpload_url() {
        return upload_url;
    }

    public void setUpload_url(String upload_url) {
        this.upload_url = upload_url;
    }

    public String getRnd_name() {
        return rnd_name;
    }

    public void setRnd_name(String rnd_name) {
        this.rnd_name = rnd_name;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucket_domail() {
        return bucket_domail;
    }

    public void setBucket_domail(String bucket_domail) {
        this.bucket_domail = bucket_domail;
    }
}
