package com.diting.model.result;

/**
 * Created by dt on 2017/1/9.
 */
public class Flag {
    private int flg;

    private int flg_count;

    private int unreadLetterNum;//未读站内信数量

    private int unreadMessageNum;//未读消息数量

    public int getFlg() {
        return flg;
    }

    public void setFlg(int flg) {
        this.flg = flg;
    }

    public int getFlg_count() {
        return flg_count;
    }

    public void setFlg_count(int flg_count) {
        this.flg_count = flg_count;
    }

    public int getUnreadLetterNum() {
        return unreadLetterNum;
    }

    public void setUnreadLetterNum(int unreadLetterNum) {
        this.unreadLetterNum = unreadLetterNum;
    }

    public int getUnreadMessageNum() {
        return unreadMessageNum;
    }

    public void setUnreadMessageNum(int unreadMessageNum) {
        this.unreadMessageNum = unreadMessageNum;
    }
}
