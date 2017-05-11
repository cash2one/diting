package com.diting.service;

import com.diting.model.wechat.WeChatPay;

/**
 * Created by Administrator on 2017/2/21.
 */
public interface WeChatPayService {
    String weixin_pay(WeChatPay weChatPay);
    String weixin_scanpay(WeChatPay weChatPay);
}
