package com.diting.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caucho.hessian.client.HessianProxyFactory;
import com.diting.service.SemanticsService;
import com.diting.service.impl.InferenceEngineServiceImpl;

import java.net.MalformedURLException;

/**
 * HessianClient
 */
public class HessianClient {
    //语义理解远程调用接口
//  private static String url = "http://60.205.59.176:8080/hessian";
    public static String url = "http://yuyi.ditingai.com/hessian";
//  public static String url = "http://localhost:8080/hessian";


    public static HessianProxyFactory factory = new HessianProxyFactory();
    public static SemanticsService semanticsService;
    static {
        try {
            semanticsService = (SemanticsService) factory.create(SemanticsService.class, url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getScene(String question) {
        InferenceEngineServiceImpl inferenceEngineService=new InferenceEngineServiceImpl();
        JSONObject shuruchuli = JSON.parseObject(semanticsService.inputHandling(question,null));
        String str = shuruchuli.get(KnowledgeUtil.qieci).toString();
        return inferenceEngineService.changjingjs(str,null);
    }
}
