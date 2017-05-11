/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.notification.sender.sms;

import com.diting.notification.message.NotificationMessage;
import com.diting.notification.sender.MessageSenderSupport;
import com.diting.util.Constants;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URI;
import java.util.regex.Pattern;

import static com.diting.util.Utils.find;
import static com.diting.util.Utils.md5;
import static com.diting.util.Utils.now;


/**
 * JinLouSMSMessageSender.
 */
public class JinLouSMSMessageSender extends MessageSenderSupport {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static final Pattern MOBILE = Pattern.compile(".*(\\d{11})");

    private PoolingHttpClientConnectionManager CONNECTION_MANAGER;
    private RequestConfig REQUEST_CONFIG;

    {
        CONNECTION_MANAGER = new PoolingHttpClientConnectionManager();
        REQUEST_CONFIG = RequestConfig.custom().build();
    }

    @Value("${sms.notification.enabled}")
    private Boolean enabled;

    @Value("${sms.jinlou.account}")
    private String smsAccount;

    @Value("${sms.jinlou.password}")
    private String smsPassword;

    @Value("${sms.jinlou.serviceUrl}")
    private String smsServiceUrl;

    public String getSmsAccount() {
        return smsAccount;
    }

    public void setSmsAccount(String smsAccount) {
        this.smsAccount = smsAccount;
    }

    public String getSmsPassword() {
        return smsPassword;
    }

    public void setSmsPassword(String smsPassword) {
        this.smsPassword = smsPassword;
    }

    public String getSmsServiceUrl() {
        return smsServiceUrl;
    }

    public void setSmsServiceUrl(String smsServiceUrl) {
        this.smsServiceUrl = smsServiceUrl;
    }

    private CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setConnectionManager(CONNECTION_MANAGER).build();
    }

    @Override
    public void send(NotificationMessage message) {
        LOGGER.info(String.valueOf(enabled));

        if (!enabled) return;

        CloseableHttpResponse response = null;
        String result = null;

        String mobile = message.getMobile();
        String content = message.getContent();

        Assert.notNull(mobile, "mobile");
        Assert.notNull(content, "content");

        String mttime = Constants.CALLRECORD_TIMESTAMP_FORMAT.format(now());

        try {
            URI uri = new URIBuilder()
                    .setPath(smsServiceUrl)
                    .setParameter("name", smsAccount)
                    .setParameter("pwd", md5(smsPassword + mttime))
                    .setParameter("content", content)
                    .setParameter("phone", find(MOBILE, mobile))
                    .setParameter("subid", "")
                    .setParameter("mttime", mttime)
                    .setParameter("rpttype", "1")
                    .build();

            HttpPost post = new HttpPost(uri);
            post.setConfig(REQUEST_CONFIG);

            response = getHttpClient().execute(post);
            result = EntityUtils.toString(response.getEntity());

            LOGGER.info("JinLou Message Result:" + result);
        } catch (Exception e) {
            throw new RuntimeException("JinLou captcha channel send verification code to  mobile [" + find(MOBILE, mobile) + "]." + " error", e);
        } finally {
            LOGGER.info("JinLou captcha channel successful send verification code to  mobile [" + find(MOBILE, mobile) + "].");
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    // ignore silently
                }
            }
        }
    }
}
