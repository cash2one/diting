package com.diting.model.wechat;

/**
 * WeChat
 */
public class Authorization{
    private String username;

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    private String auth_code;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
