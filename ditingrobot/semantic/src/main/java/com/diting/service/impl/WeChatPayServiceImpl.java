package com.diting.service.impl;

import com.diting.core.Universe;
import com.diting.dao.AccountMapper;
import com.diting.dao.OrderMapper;
import com.diting.dao.WalletLotMapper;
import com.diting.model.Order;
import com.diting.model.Qiniu;
import com.diting.model.Wallet;
import com.diting.model.WalletLot;
import com.diting.model.enums.WalletLotType;
import com.diting.model.options.WalletSearchOptions;
import com.diting.model.wechat.WeChatPay;
import com.diting.resources.request.CreditWalletRequest;
import com.diting.service.OrderService;
import com.diting.service.QiniuService;
import com.diting.service.WalletService;
import com.diting.service.WeChatPayService;
import com.diting.util.wechat.*;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.diting.util.DateUtil.getTime;
import static com.diting.util.Utils.generateToken;
import static com.diting.util.Utils.isEmpty;
import static com.diting.util.Utils.str;
import static com.diting.util.wechat.XMLUtil.doXMLParse;

/**
 * Created by Administrator on 2017/2/21.
 */
@SuppressWarnings("ALL")
@Service("weChatPayService")
@Transactional
public class WeChatPayServiceImpl implements WeChatPayService{

    @Autowired
    private QiniuService qiniuService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletLotMapper walletLotMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Value("${qr.upload.url}")
    private String QR_upload_url;

    @Override
    public String weixin_pay(WeChatPay weChatPay) {
        String str_return=null;
        // 账号信息
        String appid = PayConfigUtil.APP_ID;  // appid
        String mch_id = PayConfigUtil.MCH_ID; // 商业号
        String key = PayConfigUtil.API_KEY; // key

        String currTime = PayCommonUtil.getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        String strRandom = PayCommonUtil.buildRandom(4) + "";
        String nonce_str = strTime + strRandom;

        String order_price =weChatPay.getOrder_price().multiply(new BigDecimal(100)).toString(); // 价格   注意：价格的单位是分
        String body = weChatPay.getBody();   // 商品名称
        String out_trade_no = generateToken(28); // 订单号

        // 获取发起电脑 ip
        if (isEmpty(weChatPay.getLocalIP()))
            weChatPay.setLocalIP("127.0.0.1");
        String spbill_create_ip = weChatPay.getLocalIP();
        // 回调接口
        String notify_url = PayConfigUtil.NOTIFY_URL;
        String trade_type = "NATIVE";

        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", order_price);
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", trade_type);

        String sign = PayCommonUtil.createSign("UTF-8", packageParams,key);
        packageParams.put("sign", sign);
//        packageParams.put("balance",weChatPay.getBalance().toString());

        String requestXML = PayCommonUtil.getRequestXml(packageParams);
        System.out.println(requestXML);

        String resXml = HttpUtil.postData(PayConfigUtil.UFDODER_URL, requestXML);


        Map map = null;
        try {
            map = doXMLParse(resXml);
            String urlCode = (String) map.get("code_url");
            str_return=QRCodeUtil.encode(urlCode,"",QR_upload_url,true);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        Order order=new Order();
        order.setName(weChatPay.getBody());
        order.setUserId(Universe.current().getUserId());
        order.setOrderNumber(out_trade_no);
        order.setAppid(appid);
        order.setPaymentMethod("微信扫码支付");
        order.setBalance(weChatPay.getBalance());
        orderService.create(order);

        createWalletLog();

        //二维码上传到七牛
        Qiniu qiniu=new Qiniu();
        qiniu.setUpload_url(QR_upload_url+str_return);
        qiniu.setRnd_name(str_return);
        qiniu.setBucketName("wechat-qr-code");
        qiniu.setBucket_domail("http://olrtvlvid.bkt.clouddn.com");
        String str=qiniuService.upload_image(qiniu).getImg_url();
        return str;
    }

    @Override
    public String weixin_scanpay(WeChatPay weChatPay) {
        try {
            Map map=doXMLParse(weChatPay.getXml());
            Order order=new Order();
            order.setReturn_msg((String) map.get("return_msg"));
            order.setOrderNumber((String) map.get("out_trade_no"));
            order.setAmountOfMmoney(new BigDecimal(Integer.valueOf((String)map.get("total_fee"))/100));
            order.setTime(getTime());
            orderService.update(order);
            walletCredit(order);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }

    public static String QRfromGoogle(String chl) throws Exception {
        int widhtHeight = 300;
        String EC_level = "L";
        int margin = 0;
        chl = UrlEncode(chl);
        String QRfromGoogle = "http://chart.apis.google.com/chart?chs=" + widhtHeight + "x" + widhtHeight
                + "&cht=qr&chld=" + EC_level + "|" + margin + "&chl=" + chl;
        System.out.println("========   "+UrlEncode(QRfromGoogle)+"   ==========");
        return UrlEncode(QRfromGoogle);
    }
    // 特殊字符处理
    public static String UrlEncode(String src)  throws UnsupportedEncodingException {
        return URLEncoder.encode(src, "UTF-8").replace("+", "%20");
    }
    public void walletCredit(Order orde){
        Order order1=orderService.findByOrderNumber(orde);
        WalletSearchOptions walletSearchOptions=new WalletSearchOptions();
        walletSearchOptions.setUserId(String.valueOf(order1.getUserId()));
        Wallet wallet=walletService.search(walletSearchOptions).get(0);
        CreditWalletRequest creditWalletRequest=new CreditWalletRequest();
        creditWalletRequest.setAmount(order1.getBalance());
        creditWalletRequest.setEvent("充值");
        creditWalletRequest.setLotType(String.valueOf(WalletLotType.DIBI_WeChat));
        creditWalletRequest.setReason("微信充值");
        creditWalletRequest.setWalletId(wallet.getId());
        accountMapper.updateRecharge(order1.getUserId());
        walletService.credit(creditWalletRequest);
    }

    public void createWalletLog(){
        Integer userId=Universe.current().getUserId();
        WalletSearchOptions walletSearchOptions=new WalletSearchOptions();
        walletSearchOptions.setUserId(String.valueOf(userId));
        Wallet wallet=walletService.search(walletSearchOptions).get(0);
        WalletLot lot = walletLotMapper.getByWalletIdAndType(str(wallet.getId()), String.valueOf(WalletLotType.DIBI_WeChat));
        if (null==lot){
            WalletLot lot1 = new WalletLot();
            lot1.setWalletId(wallet.getId());
            lot1.setType(str(WalletLotType.DIBI_WeChat));
            lot1.setBalance(new BigDecimal("0.00"));
            lot1.setPriority(200);
            walletLotMapper.create(lot1);
        }
    }
}
