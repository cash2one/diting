package com.diting.model.options;

/**
 * Created by liufei on 2016/9/8.
 */
public class CustomerSynonymOptions extends PageableOptions{

    private String keywords;

    private Integer accountId;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
