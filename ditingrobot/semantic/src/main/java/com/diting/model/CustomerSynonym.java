package com.diting.model;

import com.diting.model.mongo.BaseMongoModel;

/**
 * Created by liufei on 2016/9/8.
 */
public class CustomerSynonym extends BaseMongoModel {

    private String word_old;
    private String word_new;
    private Integer account_id;

    public Integer getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Integer account_id) {
        this.account_id = account_id;
    }

    public String getWord_new() {
        return word_new;
    }

    public void setWord_new(String word_new) {
        this.word_new = word_new;
    }

    public String getWord_old() {
        return word_old;
    }

    public void setWord_old(String word_old) {
        this.word_old = word_old;
    }
}
