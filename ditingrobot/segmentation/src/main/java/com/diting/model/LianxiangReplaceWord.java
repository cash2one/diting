package com.diting.model;

/**
 * 近义词替换
 * Created by Administrator on 2017/1/3.
 */
public class LianxiangReplaceWord {
    private Integer id;
    private String pre_replace_word;
    private String replace_word;
    private String updated_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPre_replace_word() {
        return pre_replace_word;
    }

    public void setPre_replace_word(String pre_replace_word) {
        this.pre_replace_word = pre_replace_word;
    }

    public String getReplace_word() {
        return replace_word;
    }

    public void setReplace_word(String replace_word) {
        this.replace_word = replace_word;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }
}
