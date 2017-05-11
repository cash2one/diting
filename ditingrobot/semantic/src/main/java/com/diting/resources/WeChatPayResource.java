package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.wechat.WeChatPay;
import com.diting.service.WeChatPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * Created by Administrator on 2017/2/20.
 */
@Path("/wechat/pay")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class WeChatPayResource {

    @Autowired
    private WeChatPayService weChatPayService;

    //1、首先是接入微信接口，获取微信支付二维码
    @POST
    @Timed
    @Path("/")
    public String weixin_pay(WeChatPay weChatPay) throws Exception {
        return weChatPayService.weixin_pay(weChatPay);
    }

    @POST
    @Timed
    @Path("/scan")
    public String weixin_scanpay(WeChatPay weChatPay) throws Exception {
        return weChatPayService.weixin_scanpay(weChatPay);
    }

}
