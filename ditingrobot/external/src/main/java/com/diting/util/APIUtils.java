package com.diting.util;

import com.diting.model.options.ExternalOptions;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;

/**
 * APIUtils
 */
public class APIUtils {
    private static PoolingHttpClientConnectionManager CONNECTION_MANAGER;
    private static RequestConfig REQUEST_CONFIG;

    {
        CONNECTION_MANAGER = new PoolingHttpClientConnectionManager();
        REQUEST_CONFIG = RequestConfig.custom().build();
    }

    private static CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setConnectionManager(CONNECTION_MANAGER).build();
    }

    public static String post(ExternalOptions options) {
        CloseableHttpResponse response = null;
        String result = null;

        try {
            URI uri = new URIBuilder()
                    .setPath(options.getUrl())
                    .setParameter("question", options.getQuestion())
                    .setParameter("lastquestion", options.getLastQuestion())
                    .setParameter("thisScene",options.getThisScene())
                    .setParameter("lastScene",options.getLastScene())
                    .setParameter("scene",options.getScene())
                    .setParameter("kw1", options.getKw1())
                    .setParameter("kw2", options.getKw2())
                    .setParameter("kw3", options.getKw3())
                    .setParameter("kw4", options.getKw4())
                    .setParameter("kw5", options.getKw5())
                    .setParameter("uuid", options.getUuid())
                    .setParameter("userId", String.valueOf(options.getUserId()))
                    .setParameter("source",options.getSource())
                    .setParameter("uniqueId",options.getUniqueId())
                    .setParameter("ownUniqueId",options.getOwnUniqueId())
                    .build();

            HttpPost post = new HttpPost(uri);
            post.setConfig(REQUEST_CONFIG);

            response = getHttpClient().execute(post);
            result = EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
          System.out.println("error occurred during execute post method.." + e.getMessage());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    // ignore silently
                }
            }
        }
        return result;
    }

    public static String get(ExternalOptions options) {
        CloseableHttpResponse response = null;
        String result = null;

        try {
            URI uri = new URIBuilder()
                    .setPath(options.getUrl())
                    .setParameter("question", options.getQuestion())
                    .setParameter("kw1", options.getKw1())
                    .setParameter("kw2", options.getKw2())
                    .setParameter("kw3", options.getKw3())
                    .setParameter("kw4", options.getKw4())
                    .setParameter("kw5", options.getKw5())
                    .setParameter("uuid", options.getUuid())
                    .build();

            HttpPost post = new HttpPost(uri);
            post.setConfig(REQUEST_CONFIG);

            response = getHttpClient().execute(post);
            result = EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            System.out.println("error occurred during execute get method.." + e.getMessage());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    // ignore silently
                }
            }
        }
        return result;
    }
}
