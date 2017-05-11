package com.diting.model.options;

/**
 * Created by Administrator on 2017/1/21.
 */
public class QiniuOptions extends PageableOptions{
    private String img_url;
    private String message;
    private String upload_url;

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
}
