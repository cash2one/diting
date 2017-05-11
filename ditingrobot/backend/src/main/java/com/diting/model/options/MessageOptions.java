package com.diting.model.options;

import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */
public class MessageOptions extends PageableOptions{
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
