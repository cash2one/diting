package com.diting.model.options;

/**
 * Created by Administrator on 2016/12/30.
 */
public class WordBaseOptions extends PageableOptions{
    private String keyword;
    private String type;
    private String  ids;
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
