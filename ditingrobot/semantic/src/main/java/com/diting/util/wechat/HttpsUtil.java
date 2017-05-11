package com.diting.util.wechat;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

public class HttpsUtil {
 
    private static class TrustAnyTrustManager implements X509TrustManager {
 
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
 
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
 
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }
 
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
 
    /**
     * post方式请求服务器(https协议)
     * 
     * @param url
     *            请求地址
     * @param charset
     *            编码
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params, String charset)
            throws NoSuchAlgorithmException, KeyManagementException,
            IOException {
    	StringBuffer sb = new StringBuffer();
		sb.append("{");
		if (params != null) {
		for (Entry<String, String> e : params.entrySet()) {
		sb.append(e.getKey()).append(":").append(e.getValue()).append(",");
		}
		sb.substring(0, sb.length() - 1);
		}
		sb.append("}");
		String content = sb.toString();
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
                new java.security.SecureRandom());
 
        URL console = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
        conn.setSSLSocketFactory(sc.getSocketFactory());
        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        conn.setDoOutput(true);
        conn.connect();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(content.getBytes(charset));
        // 刷新、关闭
        out.flush();
        out.close();
        StringBuffer buffer = new StringBuffer();
		try {
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		String temp;
		while ((temp = br.readLine()) != null) {
		buffer.append(temp).append("\n");
		}
		} catch (Exception e) {
		}
		return buffer.toString();
    }
}
