package com.diting.model;

import java.io.Serializable;

/**
 * Created by liufei on 2016/12/28.
 */
public class InternalUser implements Serializable {
    private static final long serializable=1L;

    private Integer id;//用户ID
    private String username;//用户名
    private String password;//密码
    private String role;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
