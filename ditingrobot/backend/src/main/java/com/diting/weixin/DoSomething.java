package com.diting.weixin;

import org.springframework.stereotype.Repository;

/**
 * Created by dt on 2017/3/22.
 */
public class DoSomething implements Runnable {

    private String name;
    private String tel;

    public DoSomething(String name,String tel) {
        this.name = name;
        this.tel = tel;

    }

    public void run() {
        Dting d = new Dting();
        try {
            d.ma(name,tel);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
