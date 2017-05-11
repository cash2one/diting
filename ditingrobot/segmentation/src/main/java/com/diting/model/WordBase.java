package com.diting.model;

/**
 * Created by liufei on 2016/12/28.
 */
public class WordBase {
    private Integer id;
    private String word;//词
    private Integer word_character;//词性
    private Integer word_level;//词级
    private String comment;//备注
    private String updated_time;//更新时间
    private String create_time;//创建时间
    private Integer state;//状态 0：可用 1：不可用
    private String username;//用户名
    private String scene;//场景（可为空）

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getWord_character() {
        return word_character;
    }

    public void setWord_character(Integer word_character) {
        this.word_character = word_character;
    }

    public Integer getWord_level() {
        return word_level;
    }

    public void setWord_level(Integer word_level) {
        this.word_level = word_level;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }
}
