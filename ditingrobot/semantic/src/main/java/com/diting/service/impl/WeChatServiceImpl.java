package com.diting.service.impl;

import com.alibaba.fastjson.JSON;
import com.diting.cache.Cache;
import com.diting.cache.RedisImpl;
import com.diting.dao.AccountMapper;
import com.diting.dao.RobotMapper;
import com.diting.dao.WeChatMapper;
import com.diting.model.*;
import com.diting.model.options.WeChatAccountOptions;
import com.diting.model.wechat.*;
import com.diting.service.ChatService;
import com.diting.service.WeChatService;
import com.diting.util.KnowledgeUtil;
import com.diting.util.Utils;
import com.diting.util.wechat.HttpsUtil;
import com.diting.util.wechat.WXBizMsgCrypt;
import com.diting.util.wechat.XMLParse;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("ALL")
@Service("weChatService")
@Transactional
public class WeChatServiceImpl implements WeChatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatServiceImpl.class);

    private static final String URL_TOKEN = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
    private static final String URL_PREAUTHCODE = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=";
    private static final String URL_QUERYAUTH = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=";
    private static final String URL_SEND = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
    private static final String URL_AUTHORIZER_INFO = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=";
    private static final String encodingAesKey = "D82Ddkfjwie230234zm120239dkk233die8230k890e";
    private static final String token = "ditingai888";
    //private static final String appId = "wx570bc396a51b8ff8";https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=
    //正式环境
	public static final String REDIRECT_URI = "http://www.ditingai.com/redirect?username=";
	public static final String appId = "wx5303f1b0f3aedb9b";
	public static final String component_appsecret = "30856aeb8f99f95aa7693e56c9d1ab78";
    //测试环境
//    private static final String REDIRECT_URI = "http://www.ditingai.cn/redirect?username=";
//    public static final String REDIRECT_URI = "http://b1511c8918.iok.la/redirect?username=";
//    private static final String appId = "wx31427f4fe7c67b12";
//    private static final String component_appsecret = "48a6810cf51ed87ec973dfaa04abd857";
    private String component_access_token = "";

    @Autowired
    private WeChatMapper weChatMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RobotMapper robotMapper;

    @Autowired
    private ChatService chatService;

    @Autowired
    @Qualifier("redisCache")
    private Cache redisCache;

    @Override
    public WeChatAccount create(WeChatAccount weChatAccount) {
        weChatMapper.create(weChatAccount);
        return weChatAccount;
    }

    @Override
    public void delete(WeChatAccount weChatAccount) {
        weChatMapper.delete(weChatAccount);
    }

    @Override
    public void getInfo(WeChat weChat) throws Exception {
        String xml = weChat.getParam();
        WXBizMsgCrypt pc = null;
        pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
        Object[] cipherText = XMLParse.extract1(xml);
        String clearText = pc.decrypt(cipherText[2].toString());
        Document doc = null;
        doc = DocumentHelper.parseText(clearText);
        Element rootElt = doc.getRootElement();
        String InfoType = rootElt.elementText("InfoType");
        if (InfoType.equals("unauthorized")) {
            String app_id = rootElt.elementText("AuthorizerAppid");
            WeChatAccount weChatAccount = new WeChatAccount();
            weChatAccount.setAppId(app_id);
            unAuthorize(weChatAccount);
        } else if (InfoType.equals("component_verify_ticket")) {
            String componentVerifyTicket = rootElt.elementText("ComponentVerifyTicket");
            String access_token = getAccess_token(componentVerifyTicket);
            String component_access_token = getComponent_access_token(access_token);
            refreshToken(component_access_token);
        }
    }

    @Override
    public String authorize(Account account) throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        //component_access_token = redisImpl.strGet("component_access_token");
        component_access_token = redisCache.get("component_access_token");
        //从redis中取出component_access_token
        String url = URL_PREAUTHCODE + component_access_token;
        param.put("\"component_appid\"", "\"" + appId + "\"");
        String preAuthCode = HttpsUtil.post(url, param, "utf-8");
        String pre_auth_code = JSON.parseObject(preAuthCode).getString("pre_auth_code");
        String username = account.getUserName();
        String url_authorize = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=" + appId + "&pre_auth_code=" + pre_auth_code + "&redirect_uri=" + REDIRECT_URI + username;
        return url_authorize;
    }

    @Override
    public void redirect(Authorization authorization) throws Exception {
        String username = authorization.getUsername();
        String auth_code = authorization.getAuth_code();
        String query_auth = getQuery_auth(auth_code);
        QueryAuth queryAuth = JSON.parseObject(query_auth, QueryAuth.class);
        String authorizationInfoJson = queryAuth.getAuthorization_info();
        AuthorizationInfo authorizationInfo = JSON.parseObject(authorizationInfoJson, AuthorizationInfo.class);
        String authorizer_appid = authorizationInfo.getAuthorizer_appid();
        String refresh_token = authorizationInfo.getAuthorizer_refresh_token();
        String authorizer_infoall = getAuthorizer_infoall(authorizer_appid);
        AuthorizerInfoAll authorizerInfoAll = JSON.parseObject(authorizer_infoall, AuthorizerInfoAll.class);
        String authorizer_info = authorizerInfoAll.getAuthorizer_info();
        AuthorizerInfo authorizerInfo = JSON.parseObject(authorizer_info, AuthorizerInfo.class);
        String original_id = authorizerInfo.getUser_name();

        WeChatAccount weChatAccount = new WeChatAccount();
        weChatAccount.setAppId(authorizer_appid);
        weChatAccount.setOriginalId(original_id);
        weChatAccount.setRefreshToken(refresh_token);
        weChatAccount.setUserName(username);
        if (null == weChatMapper.get(original_id)) {
            weChatMapper.create(weChatAccount);
        }
    }

    @Override
    public String accept(AcceptParams acceptParams) throws Exception {
        String nonce = acceptParams.getNonce();
        String timeStamp = acceptParams.getTimeStamp();
        String msgSignature = acceptParams.getMsgSignature();
        WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
        String cipherText = acceptParams.getXml();
        String clearText = pc.decryptMsg(msgSignature, timeStamp, nonce, cipherText);
        Document doc = DocumentHelper.parseText(clearText);
        Element rootElt = doc.getRootElement();
        String msgType = rootElt.elementText("MsgType");
        String toUserName = rootElt.elementText("ToUserName");
        String fromUserName = rootElt.elementText("FromUserName");
        if (msgType.equals("text")) {
            String content = rootElt.elementText("Content");
            return processTextMessage(content, toUserName, fromUserName);
        } else {
            return null;
        }
    }

    public String processTextMessage(String content, String toUserName, String fromUserName) throws Exception {
        return replyTextMessage(content, toUserName, fromUserName);
    }

    public String replyTextMessage(String content, String toUserName, String fromUserName) throws Exception {
        Long createTime = Calendar.getInstance().getTimeInMillis() / 1000;

        Chat chat = this.getChat(content, toUserName,fromUserName);
        String answer=null;
        if (chat.getForbiddenEnable()){
            answer= KnowledgeUtil.ARREARAGE_ANSWER;
        }else {
            answer = chatService.getWxChat(chat).getAnswer();
        }
//        String answer="你好";
        List<String> list = new ArrayList<>();
        //获取answer
        String replyMsg = "<xml>"
                + "<ToUserName><![CDATA[" + fromUserName + "]]></ToUserName>" + "\n"
                + "<FromUserName><![CDATA[" + toUserName + "]]></FromUserName>" + "\n"
                + "<CreateTime>" + createTime + "</CreateTime>" + "\n"
                + "<MsgType><![CDATA[text]]></MsgType>" + "\n"
                + "<Content><![CDATA[" + answer + "]]></Content>" + "\n"
                + "</xml>";
        String returnvaleue = "";
        WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
        returnvaleue = pc.encryptMsg(replyMsg, createTime.toString(), "easemob");
        //response.getWriter().print(returnvaleue);
        //返回答案
        return returnvaleue;
    }

    public void unAuthorize(WeChatAccount weChatAccount) {
        weChatMapper.delete(weChatAccount);
    }

    public String getQuery_auth(String auth_code) throws Exception {
        String url = URL_QUERYAUTH + component_access_token;
        Map<String, String> param = new HashMap<String, String>();
        param.put("\"component_appid\"", "\"" + appId + "\"");
        param.put("\"authorization_code\"", "\"" + auth_code + "\"");
        String query_auth = HttpsUtil.post(url, param, "utf-8");
        return query_auth;
    }

    public String getAuthorizer_infoall(String authorizer_appid) throws Exception {
        String url_authorizer_info = URL_AUTHORIZER_INFO + component_access_token;
        Map<String, String> param = new HashMap<String, String>();
        param.put("\"component_appid\"", "\"" + appId + "\"");
        param.put("\"authorizer_appid\"", "\"" + authorizer_appid + "\"");
        String authorizer_infoall = HttpsUtil.post(url_authorizer_info, param, "utf-8");
        return authorizer_infoall;
    }

    public String getAccess_token(String ComponentVerifyTicket) throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        param.put("\"component_appid\"", "\"" + appId + "\"");
        param.put("\"component_appsecret\"", "\"" + component_appsecret + "\"");
        param.put("\"component_verify_ticket\"", "\"" + ComponentVerifyTicket + "\"");
        String access_token = HttpsUtil.post(URL_TOKEN, param, "utf-8");
        return access_token;
    }

    public String getComponent_access_token(String access_token) {
        return JSON.parseObject(access_token).getString("component_access_token");
    }

    public void refreshToken(String component_access_token) {
        redisCache.put("component_access_token", component_access_token);
    }

    @Override
    public List<Map<String,Object>> searchAllCountByTime(WeChatAccountOptions options){
        List<Map<String,Object>> results=new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer result=0;
        if(options.getType()!=null&&options.getType()!=""&&options.getType().trim().length()>0){
                if("yesterday".equals(options.getType())){
                    Date date= Utils.getDayStart(Utils.daysBefore(new Date(),1));
                    String strDate=sdf.format(date);
                    for (int i=0;i<24;i++){
                        if(i<9){
                            options.setStartTime(strDate+" 0"+i+":00:00");
                            options.setEndTime(strDate+" 0"+(i+1)+":00:00");
                        }else if(i==9){
                            options.setStartTime(strDate+" 0"+i+":00:00");
                            options.setEndTime(strDate+" "+(i+1)+":00:00");
                        }else{
                            options.setStartTime(strDate+" "+i+":00:00");
                            options.setEndTime(strDate+" "+(i+1)+":00:00");
                        }
                        Map<String,Object> map=new HashMap<>();
                        map.put("endTime",options.getEndTime());
                        result=weChatMapper.searchAllCountByTime(options);
                        map.put("count",result==null?0:result);
                        results.add(map);
                    }
                }else if("week".equals(options.getType())){
                    Date date=Utils.getDayStart(Utils.daysBefore(new Date(),1)) ;
                    String strDate=sdf.format(date);
                    for (int i=7;i>0;i--){
                        options.setStartTime(sdf.format(Utils.getDayStart(Utils.daysBefore(new Date(),i)))+" 00:00:00");
                        options.setEndTime(sdf.format(Utils.getDayStart(Utils.daysBefore(new Date(),i)))+" 24:00:00");
                        Map<String,Object> map=new HashMap<>();
                        map.put("beginTime",sdf.format(Utils.getDayStart(Utils.daysBefore(new Date(),i))));
                        result=weChatMapper.searchAllCountByTime(options);
                        map.put("count",result==null?0:result);
                        results.add(map);
                    }
                }else if("month".equals(options.getType())){
                    Date date=Utils.getDayStart(Utils.daysBefore(new Date(),1)) ;
                    for (int i=30;i>0;i--){
                        options.setStartTime(sdf.format(Utils.getDayStart(Utils.daysBefore(new Date(),i)))+" 00:00:00");
                        options.setEndTime(sdf.format(Utils.getDayStart(Utils.daysBefore(new Date(),i)))+" 24:00:00");
                        Map<String,Object> map=new HashMap<>();
                        map.put("beginTime",sdf.format(Utils.getDayStart(Utils.daysBefore(new Date(),i))));
                        result=weChatMapper.searchAllCountByTime(options);
                        map.put("count",result==null?0:result);
                        results.add(map);
                    }
                }
        }else if(options.getStartTime()!=null&&!"".equals(options.getStartTime())){
            if(options.getEndTime()==null||"".equals(options.getEndTime())){
                options.setEndTime(sdf.format(new Date()));
            }
            try {
                int days=Utils.differentDays(sdf.parse(options.getStartTime()),sdf.parse(options.getEndTime()));
                String time=options.getEndTime();
                for (int i=days;i>=0;i--){
                    Map<String,Object> map=new HashMap<>();
                    map.put("beginTime",sdf.format(Utils.getDayStart(Utils.daysBefore(sdf.parse(time),i))));
                    options.setStartTime(sdf.format(Utils.getDayStart(Utils.daysBefore(sdf.parse(time),i)))+" 00:00:00");
                    options.setEndTime(sdf.format(Utils.getDayStart(Utils.daysBefore(sdf.parse(time),i)))+" 24:00:00");
                    result=weChatMapper.searchAllCountByTime(options);
                    map.put("count",result==null?0:result);
                    results.add(map);
                }
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
        return results;
    }

    public Chat getChat(String content, String toUserName,String fromusername) {
        Chat chat = new Chat();
        WeChatAccount weChatAccount = weChatMapper.get(toUserName);
        Account account = accountMapper.getByMobile(weChatAccount.getUserName());
        Robot robot = robotMapper.getByUserId(account.getId());
        chat.setUuid(toUserName);
        chat.setQuestion(content);
        chat.setUsername(weChatAccount.getUserName());
        chat.setFromusername(fromusername);
        chat.setAccountId(account.getId());
        chat.setCompanyId(account.getCompanyId());
        chat.setRobotName(robot.getName());
        chat.setForbiddenEnable(account.getForbiddenEnable());
        chat.setSource("weChat");
        chat.setCharge_mode(robot.getChargeMode());
        chat.setIntel_calendar(robot.getIntel_calendar());
        if (robot.isEnable() != null) {
            chat.setEnable(robot.isEnable().toString());
        } else {
            chat.setEnable("false");
        }
        if (robot != null) {
            if (robot.getInvalidAnswer1() != null) {
                chat.setAnswer1(robot.getInvalidAnswer1());
            }
            if (robot.getInvalidAnswer2() != null) {
                chat.setAnswer2(robot.getInvalidAnswer2());
            }
            if (robot.getInvalidAnswer3() != null) {
                chat.setAnswer3(robot.getInvalidAnswer3());
            }
            if (robot.getInvalidAnswer4() != null) {
                chat.setAnswer4(robot.getInvalidAnswer4());
            }
            if (robot.getInvalidAnswer5() != null) {
                chat.setAnswer5(robot.getInvalidAnswer5());
            }
        }
        return chat;
    }
    public static void main(String[] args){
        Cache redisCache = new RedisImpl();
        redisCache.put("component_access_token", "-LuBl_dBQu-ZlIgF4mRqd8btDeMQreB2cqORLtEa01Xcx-tmGwov3Yekx9VsOf1gqm_KkP77WbW2zZVxnXvKDQfjRIi26zvDYOAbabV3N_3C0ooMr3y-X02QP3bzE31nDEUcACAIJZ");
    }
}
