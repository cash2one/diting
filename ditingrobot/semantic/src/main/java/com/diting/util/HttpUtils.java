package com.diting.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpUtils {

    private HashSet<String> sensitiwordHashSet;

    public static String sendRequestWithHttpURLConnection(String url, String json) {
        String response = null;
        try {
            URL Url = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) Url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            // POST请求
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(json.getBytes());
            out.flush();
            out.close();

            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            reader.close();
            // 断开连接
            connection.disconnect();
            response = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private void readfile() {
        sensitiwordHashSet = new HashSet<String>();
        String str;
        // FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(
                    new FileInputStream("f://diting//abc.txt"));
            bufferedReader = new BufferedReader(inputStreamReader);
            while ((str = bufferedReader.readLine()) != null) {
                sensitiwordHashSet.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally { // 关闭流
            if (bufferedReader != null)
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }


    }

    public static void main(String[] args) {

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.readfile();
        JSONObject json = new JSONObject();
        int i = 0;
        String str_uuid="fGkEafYjcNghhFgbXOGKrfvUYMaGAghT";
        for (String str : httpUtils.sensitiwordHashSet) {
            i++;
            if (i%35==0){
                    str_uuid="fGkEafUIoWRYrtjgbXfeKrweUYMaGAghT";
            }else if (i%99==0){
                str_uuid="fGRGafYjcNdrertFVXrghrHvUYGHGAghT";
            }else if (i%120==0){
                str_uuid="fGkEafYjcNghDWESeGFBHjvUYMaGAghT";
            }else if (i%134==0){
                str_uuid="fGkEafYjcNAWERgbWDFtrWyhnYmnAghT";
            }else if (i%179==0){
                str_uuid="fGkEafYWDReFFWERTTghrfvUkjaGAghT";
            }else if (i%235==0){
                str_uuid="fGkEafYjcWazxjTYUlEdRfjhFMaGAghT";
            }
            try {
                Thread.sleep(1000);// 睡眠100毫秒
                json.put("uuid", str_uuid);   //随机字符串 保证唯一即可
                json.put("question", str);
                json.put("username", "18348024817");   //换成自己注册的谛听开发平台账号
                System.out.println(str);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String s = HttpUtils.sendRequestWithHttpURLConnection("http://www.ditingai.com/remote/chat/info", json.toString());
            if (s != null) {
                try {
                    JSONObject jsonobject = new JSONObject(s.toString());
                    String answer = jsonobject.getString("answer");           //得到答案
                    String action = jsonobject.getJSONObject("action").getString("actionOption");     //得到动作
                    System.out.println("answer:" + answer);
                    System.out.println("action:" + action);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

}