package com.diting.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import static com.diting.util.Utils.isEmpty;

/**
 * RemoteUtils
 */
public class RemoteUtils {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 300000;
    public static final int DEF_READ_TIMEOUT = 300000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";


    public static String getRequest1(String str, String url) {
        String result = null;
        String str_return = null;
        try {
            Map<String, String> stringMap = new HashMap<>();
            stringMap.put("keyword" , "你好");
            result = net(url, stringMap, "GET");
            JSONObject jsonObject = JSON.parseObject(result);
            Set<String> set = jsonObject.keySet();
            String key = null;
            for (String string : set) {
                String[] str_key = string.split(",");
                for (String s : str_key) {
                    if (s.equals(str)) {
                        key = string;
                        break;
                    }
                }
                if (key != null) {
                    break;
                }
            }
            if (!isEmpty(key)) {
                JSONArray object = jsonObject.getJSONArray(key);
                JSONObject jsonObject1 = (JSONObject) object.get(0);
                str_return = String.valueOf(jsonObject1.get("replace_word"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str_return;
    }

    public static List<String> replaceAssociation(String replaceWord, String url) {
        String[] kw = replaceWord.split(",");
        String str = kw[0];
        List<String> stringList = new ArrayList<>();
//        for (int i = 0; i < kw.length; i++) {
//            String replaceAssociation = getRequest1(kw[i],url);
//            if (i == 0) {
//                if (!isEmpty(replaceAssociation)) {
//                    str = replaceAssociation;
//                }
//            } else if (!isEmpty(replaceAssociation)) {
//                str = str + "," + replaceAssociation;
//            } else {
//                str = str + "," + kw[i];
//            }
//
//        }
        List<List<String>> lists = new ArrayList<>();
        for (int i = 0; i < kw.length; i++) {
            List<String> stringList1 = new ArrayList<>();
            String replaceAssociation = getRequest1(kw[i], url);
            stringList1.add(kw[i]);
            if (!isEmpty(replaceAssociation)) {
                stringList1.add(replaceAssociation);
            }
            lists.add(stringList1);
        }

        for (String s0 : lists.get(0)) {
            for (String s1 : lists.get(1)) {
                for (String s2 : lists.get(2)) {
                    for (String s3 : lists.get(3)) {
                        for (String s4 : lists.get(4)) {
                            str=s0+","+s1+","+s2+","+s3+","+s4;
                            stringList.add(str);
                        }
                    }
                }
            }
        }
        return stringList;
    }

    public static String getRequest2(String str, String url) {
        String result = null;
        String str_return = null;
        try {
            Map<String, String> stringMap = new HashMap<>();
            stringMap.put("keyword" , "你好");
            result = net(url, stringMap, "GET");
            Collection<Object> collection = JSON.parseObject(result).values();
            for (Object object : collection) {
                JSONObject jsonObject1 = (JSONObject) JSON.parseArray(object.toString()).get(0);
                if (jsonObject1.get("replace_word").equals(str)) {
                    str_return = (String) jsonObject1.get("pre_replace_word");
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return str_return;
    }


    public static List<String> synonymReplacement(String keyword, String url) {
        List<String> strings = Arrays.asList(keyword.split(","));
        List<String[]> returnList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        for (String str : strings) {
            if (!str.equals("k$z")) {
                String str_request = getRequest2(str, url);
                if (!isEmpty(str_request)) {
                    String[] strings1 = str_request.split(",");
                    returnList.add(strings1);
                } else {
                    returnList.add(str.split(","));
                }
            } else {
                returnList.add(str.split(","));
            }
        }
        for (int i = 0; i < returnList.get(0).length; i++) {
            for (int i1 = 0; i1 < returnList.get(1).length; i1++) {
                for (int i2 = 0; i2 < returnList.get(2).length; i2++) {
                    for (int i3 = 0; i3 < returnList.get(3).length; i3++) {
                        for (int i4 = 0; i4 < returnList.get(4).length; i4++) {
                            String s = returnList.get(0)[i] + "," + returnList.get(1)[i1] + "," + returnList.get(2)[i2] + "," + returnList.get(3)[i3] + "," + returnList.get(4)[i4];
                            list.add(s);
                        }
                    }
                }
            }
        }
        return list;
    }

    public static void main(String[] args) {
//        String s = sendRequestWithHttpURLConnection("http://localhost:8080/admin/lianxiangReplaceWord/listAll.do",null);
//        synonymReplacement("微信,岁,k$z,老板,几");
//        JSONObject jsonObject= JSON.parseObject(s);
//        System.out.println(s);
    }

    /**
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return 网络请求字符串
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static String net(String strUrl, Map params, String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if (method == null || method.equals("GET")) {
                strUrl = strUrl + "?" + urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (method == null || method.equals("GET")) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent" , userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params != null && method.equals("POST")) {
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
    public static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "" , "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
