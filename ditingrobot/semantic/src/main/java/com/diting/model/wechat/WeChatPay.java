package com.diting.model.wechat;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/2/22.
 */
public class WeChatPay {
    private BigDecimal order_price;//商品价格
    private String body;//商品名称
    private String localIP;
    private String xml;
    private BigDecimal balance;//充值点数

    public BigDecimal getOrder_price() {
        return order_price;
    }

    public void setOrder_price(BigDecimal order_price) {
        this.order_price = order_price;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLocalIP() {
        return localIP;
    }

    public void setLocalIP(String localIP) {
        this.localIP = localIP;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
