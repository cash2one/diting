package com.diting.util;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liufei on 2016/9/7.
 */
public class SemanticsUtil {

    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 300000;
    public static final int DEF_READ_TIMEOUT = 300000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    //1.事件列表
    public static String getRequest1(String url,Map<String,String> map){
        String result =null;
        try {
            result =net(url, map, "GET");
//            JSONObject jsonObject = JSON.parseObject(result);
//            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
//        String url ="http://60.205.208.60:8080/remote/semantics/inputHandling.do";//请求接口地址
        String url ="http://101.201.208.60/remote/semantics/semanticMyopia.do";
        Map<String,String> params = new HashMap();//请求参数
        params.put("a","你 有 什么 功能");//问题
        params.put("b","你有哪些功能,还以为你了解小谛呢，原来你没把人家放在心上呀，你要多来陪我哦,6,你会什么,156491fen!ge你有哪些功能,小笨笨，还以为你知道很多呢，这都不知道呀，多来陪我，我就告诉你！,6,你会什么,26034fen!ge你有哪些功能,亲，不要告诉我你理解有障碍哦，提示写的清清楚楚的,5,你会什么,89160fen!ge你有哪些功能,还以为你了解小谛呢，原来你没把人家放在心上呀,5,你会什么,156493fen!ge你有哪些功能,提示里不是写的清清楚楚吗？看了之后再问，ＯＫ。？,5,你会什么,67314fen!ge你有哪些功能,提示上写的清清楚楚啊孩纸,4,你会什么,67307fen!ge你有哪些功能,我会讲笑话，会背唐诗绝句,5,你会什么,360551fen!ge你有哪些功能,我怀疑你理解有障碍哦，看看提示吧！,3,你会什么,53108fen!ge你有哪些功能,不是有提示吗？自己不会看啊亲,5,你会什么,168830fen!ge你有哪些功能,就不告诉你，怎样？,1,你会什么,146995fen!ge你有什么功能呢,你多用用就知道啦！,5,你会什么,313831fen!ge你有什么功能,功能很多，你可以试试。,5,你会什么,313829fen!ge你有哪些功能,亲，你已经问过很多遍了，看看提示再问行吗？,5,你会什么,88978fen!ge你有哪些功能,问这么多遍，就不会看看提示？,2,你会什么,62357fen!ge你有哪些功能,你不是聪明吗，猜呀，嘻嘻！,3,你会什么,131231fen!ge你有哪些功能,陪聊，讲笑话，背诗,5,你会什么,360569fen!ge你有哪些功能,不是有提示吗？自己不会看啊小笨笨,6,你会什么,168829fen!ge你有哪些功能,哇哦，这傻孩纸，又没看提示，真是不让人省心呢，嘻嘻,6,你会什么,65489fen!ge你有哪些功能,哇哦，这傻孩纸，又没看提示，真是不让人省心呢,5,你会什么,65494fen!ge你有哪些功能,亲爱哒，你已经问过很多遍了，看看提示再问行吗？,6,你会什么,88554fen!ge你有哪些功能,不要告诉我你理解有障碍哦，提示写的清清楚楚的,4,你会什么,168267fen!ge你有哪些功能,哇哦，又没看提示，真是不让人省心呢，,4,你会什么,65496fen!ge你有哪些功能,哇哦，又没看提示，该怎么说你呢,3,你会什么,65500fen!ge你有哪些功能,请认真阅读提示啊笨笨…,5,你会什么,86417fen!ge你有哪些功能,有提示，自己不会看啊？,3,你会什么,16452fen!ge你有什么功能,我是北京谛听机器人科技有限公司的客服机器人。回答各类问题。,5,你会什么,359213fen!ge你有什么功能,主要功能是陪你聊天，但是聊天也分很多种对吧！讲笑话是一绝,5,你会什么,363427fen!ge你有什么功能呀,功能很多，你可以试试。,5,你会什么,313830fen!ge你有什么功能,聊天讲笑话放音乐。,6,你会什么,111865fen!ge你有什么功能,我什么都会,6,你会什么,45834fen!ge你有哪些功能,首先就是陪你聊天啊，然后还会讲笑话，背唐诗，不过暂时还是只会背绝句。,5,你会什么,361426fen!ge你有哪些功能,不是有提示吗？自己不会看啊,4,你会什么,168831");//问题
        getRequest1(url,params);
    }

    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static String net(String strUrl, Map params, String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String,Object>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}
