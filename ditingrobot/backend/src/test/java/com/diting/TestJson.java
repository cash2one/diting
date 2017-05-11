package com.diting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2016/8/7.
 */
public class TestJson {
    public static void main(String args[]){
        String string="{\"qieci\":\"你 好\"}";
        JSONObject jsonObject= JSON.parseObject(string);
        System.out.print(jsonObject.get("qieci"));
    }
}
