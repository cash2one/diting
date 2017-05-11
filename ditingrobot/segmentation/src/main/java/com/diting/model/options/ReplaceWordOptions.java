package com.diting.model.options;

/**
 * Created by Administrator on 2017/1/3.
 */
public class ReplaceWordOptions extends PageableOptions{
    private String keyword;
    private String  ids;
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
