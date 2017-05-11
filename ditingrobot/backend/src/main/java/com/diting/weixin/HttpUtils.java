package com.diting.weixin;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;



public class HttpUtils {
    public static String sendRequestWithHttpURLConnection(String url,String json) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
        String response = null;
        try {
            StringEntity s = new StringEntity(json,"UTF-8");
            httpPost.setEntity(s);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                response = EntityUtils.toString(entity, "UTF-8");
                //response = JSON.parseObject(response).getString("answer");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "我是机器人，很高兴为您服务！";
        }
        return response;
    }
    
    public static void main(String[] args) {
//    	JSONObject json = new JSONObject();
//        json.put("uuid","ssdeesds");
////        json.put("question","何处");
////        json.put("username","18612790717");
//
//
//        json.put("question","你们公司名字");
//        json.put("username","18311065026");
 
   // 	String s = HttpUtils.sendRequestWithHttpURLConnection("http://l153369y82.iask.in/api/chat/info",json.toString());
//        for(int i = 0;i<400;i++){
//        	String s = HttpUtils.sendRequestWithHttpURLConnection("http://www.ditingai.com/remote/chat/info",json.toString());
//        	System.out.println(s);
//        }

        
    }
    
}