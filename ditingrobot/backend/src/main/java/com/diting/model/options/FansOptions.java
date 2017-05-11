package com.diting.model.options;

/**
 * Created by Administrator on 2017/3/6.
 */
public class FansOptions extends PageableOptions {
    private String own_phone;
    private String oth_ptone;

    public String getOwn_phone() {
        return own_phone;
    }

    public void setOwn_phone(String own_phone) {
        this.own_phone = own_phone;
    }

    public String getOth_ptone() {
        return oth_ptone;
    }

    public void setOth_ptone(String oth_ptone) {
        this.oth_ptone = oth_ptone;
    }
}
