package com.diting.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.diting.cache.Cache;
import com.diting.dao.AccountMapper;
import com.diting.dao.CustomerSynonymMapper;
import com.diting.dao.RobotMapper;
import com.diting.model.Account;
import com.diting.model.Chat;
import com.diting.model.CustomerSynonym;
import com.diting.service.AnswerConstructorService;
import com.diting.service.InferenceEngineService;
import com.diting.service.PinYinHuaService;
import com.diting.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

import static com.diting.util.Utils.isEmpty;

@SuppressWarnings("ALL")
@Service("inferenceEngineService")
@Transactional
public class InferenceEngineServiceImpl extends HessianClient implements InferenceEngineService {
    private final Logger LOGGER = LoggerFactory.getLogger(InferenceEngineServiceImpl.class);

    @Autowired
    @Qualifier("redisCache")
    private Cache redisCache;


    @Autowired
    AnswerConstructorService answerConstructorService;

    @Autowired
    RobotMapper robotMapper;

    @Autowired
    CustomerSynonymMapper customerSynonymMapper;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    PinYinHuaService pinYinHuaService;

    @Value("${remote.input.url}")
    private String input_url;

    @Value("${remote.scene.url}")
    private String inputhandle_scene_url;

    @Value("${remote.keywords.url}")
    private String keyword_url;

    @Value("${remote.utils.url}")
    private String remote_url;

    /**
     * @param C2 上句场景
     * @param N2 上句知识库缓存
     * @param R2 获取上句输入
     * @param G2 获取上句关键字
     * @param D2 获取上句答案
     * @param G3 获取上上句关键字
     * @param R3 获取上上句输入
     * @return
     * @throws SQLException
     */
    public Map<String, String> tuiliji(Chat chatInfo, String C2, String N2, String R2, String G2, String D2, String G3, String R3) {
        String bencishuru = chatInfo.getQuestion();
        Map<String, String> map = new HashMap<>();
        Chat chatCache = new Chat();//用于存放聊天缓存
        String isDisable = chatInfo.getAvailable();
        List<Chat> chatList = new ArrayList<>();
        if (chatInfo.getUuid() != null) {
            Date date = Utils.now();
            chatList = redisCache.get(chatInfo.getUuid());
            Date date1 = Utils.now();
//            LOGGER.info("tuiliji first time consuming :" + Utils.timeDifference(date, date1));
        }
        chatCache.setUuid(chatInfo.getUuid());
        chatCache.setQuestion(chatInfo.getQuestion());
        String companyId = String.valueOf(chatInfo.getCompanyId());
        if (companyId == null || companyId.equals("null")) {
            Account account = accountMapper.getByMobile(chatInfo.getUsername());
            if (null == account) {
                companyId = "-1";
            } else {
                companyId = String.valueOf(account.getCompanyId());
            }
        }
        if (chatList != null && chatList.size() > 0 && bencishuru != null && bencishuru.equals("继续")) {
            //从缓存里获取上句问题
            for (int i = chatList.size() - 1; i >= 0; i--) {
                if (!chatList.get(i).getQuestion().equals("继续")) {
                    bencishuru = chatList.get(i).getQuestion();
                    break;
                }
            }
        }
        //同义算法 B库企业同义词处理（把用户输入切词后做企业同义词替换，知识库问题不做企业同义词替换）
        String key = "CustomerSynonym" + chatInfo.getUsername();
        List<CustomerSynonym> customerSynonymList = new ArrayList<>();
        customerSynonymList = redisCache.get(key);
//        if (customerSynonymList == null || customerSynonymList.size() == 0) {
//            customerSynonymList = this.getCustomerSynonymList();
//        }
        if (null != customerSynonymList && customerSynonymList.size() > 0) {
            for (int j = 0; j < customerSynonymList.size(); j++) {
                if (bencishuru.contains(customerSynonymList.get(j).getWord_old())) {
                    bencishuru = bencishuru.replace(customerSynonymList.get(j).getWord_old(), customerSynonymList.get(j).getWord_new());
//                    System.out.println(bencishuru);
                }
            }
        }
        //获取本次用户输入，切词后
        Map<String, String> remote_map = new HashMap<>();
        remote_map.put("question", bencishuru);
//        String str_scene = changjingjs(wenti1, null);//这里计算本句场景
        remote_map.put("scene", C2);
        String qieci_result = SemanticsUtil.getRequest1(input_url, remote_map);
        String T = JSON.parseObject(qieci_result).getString("qieci");
        remote_map.remove("question");
//        String T = JSON.parseObject(semanticsService.inputHandling(bencishuru,C2)).getString("qieci");

        String outputText = "";

        String R4 = "";
        String G4 = "";
        String yu = "";
        String gjz1 = "";
        String gjz3 = "";
        String wenti3 = "";
        String gjz4 = "";
        String wenti4 = "";
        String gjz5 = "";
        String wenti5 = "";
        String C5 = "";

        String gjz7 = "";
        String wenti7 = "";

        if ((C2 == null || C2.isEmpty()) && T.equals(""))//当本剧输入为空并且上句场景为空，这里设定默认为题为“你好”
        {
            T = "你好";
        }
        String wenti1 = T;
        String wenti100 = "";

        //这里进行代词替换
//        if (outputText.equals("")) {
//            wenti1 = wenti1.trim();
//            if (G2 != null && !G2.equals("")) {
//                String a3[] = G2.split(",");
//                if (!G2.equals("") && (wenti1.indexOf("这") > -1 || wenti1.indexOf("那") > -1 || wenti1.indexOf("他") > -1 || wenti1.indexOf("她") > -1 || wenti1.indexOf("它") > -1) && (wenti1.indexOf("们") < 0)) {
//                    wenti1 = daichichuli(wenti1, a3[0], a3[1]);//疑问1:为什么进行代词替换
//                }
//            }
//        }
        wenti100 = wenti1;
        //这里计算本句关键字
        remote_map.put("keyword", wenti1);
        String keywords = SemanticsUtil.getRequest1(keyword_url, remote_map);
        JSONObject jsonObject = JSON.parseObject(keywords);
        String new_kw1 = jsonObject.getString("gjz1");
        String new_kw2 = jsonObject.getString("gjz2");
        String new_kw3 = jsonObject.getString("gjz3");
        String new_kw4 = jsonObject.getString("gjz4");
        String new_kw5 = jsonObject.getString("gjz5");
        remote_map.remove("keyword");
        gjz1 = new_kw1 + "," + new_kw2 + "," + new_kw3 + "," + new_kw4 + "," + new_kw5;
        chatCache.setKeywords(gjz1);

        C5 = changjingjs(wenti1, null);//这里计算本句场景
        chatCache.setScene(C5);
        //整句匹配（用户输入和问题两个去掉标点符号以后做整句匹配）（上句场景）
        if (isEmpty(yu)){
            yu = answerConstructorService.findByQuestionAndScene(bencishuru, C2, isDisable, companyId);
        }
        if (yu != null && !yu.equals("")) {
            outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
            if (outputText == null || outputText.equals("")) {
                yu = "";
            }
        }

        if ((outputText == null || outputText == "")) {
            yu = answerConstructorService.findByQuestionAndScene(bencishuru, C5, isDisable, companyId);
            if (yu != null && yu != "") {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }
        //整句匹配（用户输入和问题两个去掉标点符号以后做整句匹配）（上句场景）
        if (isEmpty(yu)){
            yu = answerConstructorService.findByHandleQuestionAndScene(T.replace(" ",""), C2, isDisable, companyId);
        }
        if (yu != null && !yu.equals("")) {
            outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
            if (outputText == null || outputText.equals("")) {
                yu = "";
            }
        }

        if ((outputText == null || outputText == "")) {
            yu = answerConstructorService.findByHandleQuestionAndScene(T.replace(" ",""), C5, isDisable, companyId);
            if (yu != null && yu != "") {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }

        //第一种情况：重新计算yu----标准处理
        if (yu == null || yu.equals("")) {
            yu = answerConstructorService.yutou(gjz1, C2, isDisable, companyId);
        }
        if (yu != null && !yu.equals("")) {
            outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
            if (outputText == null || outputText.equals("")) {
                yu = "";
            }
        }
        //第二种情况：场景变化的标准处理

        if ((outputText == null || outputText == "")) {
            yu = answerConstructorService.yutou(gjz1, C5, isDisable, companyId);
            if (yu != null && yu != "") {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }

        //第三种情况 添加联想算法【上句场景】==调用词库系统 联想替换 关键字2~5替换 根据场景进行查询【五个关键字+场景查询】
        List<String> str_gjz1 = RemoteUtils.replaceAssociation(gjz1, remote_url);
        if (outputText == null || outputText.equals("")) {
            for (String str1 : str_gjz1) {
                yu = answerConstructorService.yutou(str1, C2, isDisable, companyId);
                if (!isEmpty(yu)) {
                    outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                    if (isEmpty(outputText)) {
                        yu = "";
                    } else {
                        break;
                    }
                }
            }

        }
        //第四种情况 添加联想算法[本句场景]==调用词库系统 联想替换 关键字2~5替换 根据场景进行查询【五个关键字+场景查询】
        if (outputText == null || outputText.equals("")) {
            for (String str1 : str_gjz1) {
                yu = answerConstructorService.yutou(str1, C5, isDisable, companyId);
                if (!isEmpty(yu)) {
                    outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                    if (isEmpty(outputText)) {
                        yu = "";
                    } else {
                        break;
                    }
                }
            }
        }

        //第五种情况 B库反向联想查询（上句场景，kw1,kw2,kw3,kw4,kw5做近义词反向替换，一替多）
        List<String> str_gjzList = RemoteUtils.synonymReplacement(gjz1, remote_url);
        if (outputText == null || outputText.equals("")) {
            for (String s_gjz : str_gjzList) {
                yu = answerConstructorService.yutou(s_gjz, C2, isDisable, companyId);
                if (!isEmpty(yu)) {
                    break;
                }
            }
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }
        //第六种情况 B库反向联想查询（本句场景，kw1,kw2,kw3,kw4,kw5做近义词反向替换，一替多）
        if (outputText == null || outputText.equals("")) {
            for (String s_gjz : str_gjzList) {
                yu = answerConstructorService.yutou(s_gjz, C5, isDisable, companyId);
            }
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }


        //第十二 11.	B库半模糊查询（上句场景，只查kw1,kw2,kw3,kw4）
        if (outputText.equals("")) {
//            System.out.println("gjz1:"+gjz1+"   || C2:"+C2+" isDisable:"+isDisable+"userId:"+userId);
            yu = answerConstructorService.yusanOblect(gjz1, C2, isDisable, companyId);
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }
        //第十三	B库半模糊查询（本句场景，只查kw1,kw2,kw3,kw4）
        if (outputText.equals("")) {

            yu = answerConstructorService.yusanOblect(gjz1, chatCache.getScene(), isDisable, companyId);
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }

        //第二十一 语音相近处理   //上句场景相同的所有答案，找出语音最接近的一个
        if (isEmpty(outputText) && !isEmpty(C2)) {
            String kws = answerConstructorService.findKnowledgeByScene(C2, isDisable, companyId);
            String kw = pinYinHuaService.gjzyyxiangsizhi(kws, gjz1);
            if (!isEmpty(kw)) {
                yu = answerConstructorService.yutou(kw, C2, isDisable, companyId);
                if (yu != null && !yu.equals("")) {
                    outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                    if (outputText == null || outputText.equals("")) {
                        yu = "";
                    }
                }
            }
        }

        //第七种情况: 推理判断
        String a0[] = null;
        //这里计算本句关键字
        a0 = gjz1.split(",");
        if (outputText.equals("") && (a0[0].equals("k$z") || a0[1].equals("k$z") || a0[2].equals("k$z")) && G2 != null && !G2.equals("") && (C2 != null && !C2.equals("")) && !R2.equals("")) {
            if (!wenti100.equals("") && !D2.equals("") && outputText.equals("")) {
                String gjz100 = shangjvgjzjs(D2);
                if (shangjvpd(gjz100)) {
                    wenti5 = wendabuquan(wenti100, gjz100);//T1 主语补全
                    chatCache.setQuestion(wenti5);
                    chatCache.setWendabuquan(wenti5);

                    remote_map.put("keyword", wenti5);
                    String keywords5 = SemanticsUtil.getRequest1(keyword_url, remote_map);
                    JSONObject jsonObject5 = JSON.parseObject(keywords5);
                    String str_new_kw1 = jsonObject.getString("gjz1");
                    String str_new_kw2 = jsonObject.getString("gjz2");
                    String str_new_kw3 = jsonObject.getString("gjz3");
                    String str_new_kw4 = jsonObject.getString("gjz4");
                    String str_new_kw5 = jsonObject.getString("gjz5");
                    remote_map.remove("keyword");

                    gjz5 = str_new_kw1 + "," + str_new_kw2 + "," + str_new_kw3 + "," + str_new_kw4 + "," + str_new_kw5;
//                    JSONObject jsonObject5 = JSON.parseObject(semanticsService.keywordProcessing(wenti5));
//                    StringBuffer stringBuffer5 = new StringBuffer();
//                    for (int f = 1; f <= jsonObject5.size(); f++) {
//                        stringBuffer5.append(new StringBuffer(jsonObject5.getString("gjz" + f) + ","));
//                    }
//                    gjz5 = stringBuffer5.toString().substring(0, stringBuffer5.length() - 1);
                    String CJ5 = changjingjs(wenti5, C2);

                    //场景放入session20160618
                    chatCache.setScene(CJ5);
                    if (yu == null || yu.equals("")) {
                        yu = answerConstructorService.yusan(gjz5, CJ5, isDisable, companyId);
                    }
                    if (yu != null && !yu.equals("")) {
                        outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti5, yu)).getString("xiangsijieguo");
                        if (outputText == null || outputText.equals("")) {
                            yu = "";
                        }
                    }
                }
                if (!outputText.equals("")) {
                    wenti1 = wenti5;
                }
            }
            //T2 句子其它补全
            String a1[] = G2.split(",");
            if (outputText.equals("") && (!a0[0].equals("k$z") || !a0[1].equals("k$z")) && (a0[0].equals("k$z") || a0[1].equals("k$z")) && (!a1[0].equals("k$z") || !a1[1].equals("k$z"))) {
                if (R3 != null && !R3.equals("") && G3 != null && !G3.equals("") && shengluepanduan1(gjz1, G3)) {
                    G2 = G3;
                    R2 = R3;
                }
                wenti3 = shengluebuquan(gjz1, G2);
                chatCache.setQuestion(wenti3);
                JSONObject jsonObject3 = JSON.parseObject(semanticsService.keywordProcessing(wenti3));
                StringBuffer stringBuffer3 = new StringBuffer();
                for (int f = 1; f <= jsonObject3.size(); f++) {
                    stringBuffer3.append(new StringBuffer(jsonObject3.getString("gjz" + f) + ","));
                }
                gjz3 = stringBuffer3.toString().substring(0, stringBuffer3.length() - 1);
                if (yu == null || yu.equals("")) {
                    yu = answerConstructorService.yuer(gjz3, isDisable, companyId);
                }
                if (yu != null && !yu.equals("")) {
                    outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti3, yu)).getString("xiangsijieguo");
                    if (outputText == null || outputText.equals("")) {
                        yu = "";
                    }
                }

                if (outputText.equals("")) {
                    if (yu == null || yu.equals("")) {
                        yu = answerConstructorService.yuzuObject(gjz3, isDisable, companyId);
                    }
                    if (yu != null && !yu.equals("")) {
                        outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti3, yu)).getString("xiangsijieguo");
                        if (outputText == null || outputText.equals("")) {
                            yu = "";
                        }
                    }
                }

                if (outputText.equals("")) {
                    String CJ2 = changjingjs(wenti3, C2);
                    chatCache.setScene(CJ2);
                    if (yu == null || yu.equals("")) {
                        yu = answerConstructorService.yusan(gjz3, CJ2, isDisable, companyId);
                    }
                    if (yu != null && !yu.equals("")) {
                        outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti3, yu)).getString("xiangsijieguo");
                        if (outputText == null || outputText.equals("")) {
                            yu = "";
                        }
                    }
                }
                if (!outputText.equals("")) {
                    wenti1 = wenti3;
                }
            }
            if (outputText.equals("")) {
                if (!R4.equals("") && !G4.equals("")) {
                    G2 = G4;
                    R2 = R4;
                }
                String a4[] = G2.split(",");
                if (a0[1].equals("k$z") && !a4[1].equals("k$z")) {
                    wenti4 = buquanjvzi(wenti1, R2, gjz1, G2, 1);
                    chatCache.setQuestion(wenti4);
                    JSONObject jsonObject4 = JSON.parseObject(semanticsService.keywordProcessing(wenti4));
                    StringBuffer stringBuffer4 = new StringBuffer();
                    for (int f = 1; f <= jsonObject4.size(); f++) {
                        stringBuffer4.append(new StringBuffer(jsonObject4.getString("gjz" + f) + ","));
                    }
                    gjz4 = stringBuffer4.toString().substring(0, stringBuffer4.length() - 1);
                    chatCache.setKeywords(gjz4);
                    if (yu == null || yu.equals("")) {
                        yu = answerConstructorService.yuer(gjz4, isDisable, companyId);
                    }
                    if (yu != null && !yu.equals("")) {
                        outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti4, yu)).getString("xiangsijieguo");
                        if (outputText == null || outputText.equals("")) {
                            yu = "";
                        }
                    }

                    if (outputText.equals("")) {
                        String str_C2 = changjingjs(wenti4, C2);
                        if (yu == null || yu.equals("")) {
                            yu = answerConstructorService.yusan(gjz4, str_C2, isDisable, companyId);
                        }
                        if (yu != null && !yu.equals("")) {
                            outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti4, yu)).getString("xiangsijieguo");
                            if (isEmpty(outputText)) {
                                yu = "";
                            } else {
                                chatCache.setScene(str_C2);
                            }
                        }
                    }
                }
                if (outputText.equals("") && a0[0].equals("k$z") && !a4[0].equals("k$z")) {
                    wenti4 = buquanjvzi(wenti1, R2, gjz1, G2, 0);
                    JSONObject jsonObject4 = JSON.parseObject(semanticsService.keywordProcessing(wenti4));
                    StringBuffer stringBuffer4 = new StringBuffer();
                    for (int f = 1; f <= jsonObject4.size(); f++) {
                        stringBuffer4.append(new StringBuffer(jsonObject4.getString("gjz" + f) + ","));
                    }
                    gjz4 = stringBuffer4.toString().substring(0, stringBuffer4.length() - 1);
                    if (yu == null || yu.equals("")) {
                        yu = answerConstructorService.yuer(gjz4, isDisable, companyId);
                    }
                    if (yu != null && !yu.equals("")) {
                        outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti4, yu)).getString("xiangsijieguo");
                        if (outputText == null || outputText.equals("")) {
                            yu = "";
                        }
                    }
                    if (outputText.equals("")) {
                        String str_c2 = changjingjs(wenti4, C2);
                        if (yu == null || yu.equals("")) {
                            yu = answerConstructorService.yusan(gjz4, str_c2, isDisable, companyId);
                        }
                        if (yu != null && !yu.equals("")) {
                            outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti4, yu)).getString("xiangsijieguo");
                            if (isEmpty(outputText)) {
                                yu = "";
                            } else {
                                chatCache.setScene(C2);
                            }
                        }
                    }
                }

                if (outputText.equals("") && a0[0].equals("k$z") && !a4[1].equals("k$z")) {
                    wenti4 = buquanjvzi(wenti1, R2, gjz1, G2, 1);
                    JSONObject jsonObject4 = JSON.parseObject(semanticsService.keywordProcessing(wenti4));
                    StringBuffer stringBuffer4 = new StringBuffer();
                    for (int f = 1; f <= jsonObject4.size(); f++) {
                        stringBuffer4.append(new StringBuffer(jsonObject4.getString("gjz" + f) + ","));
                    }
                    gjz4 = stringBuffer4.toString().substring(0, stringBuffer4.length() - 1);
                    if (yu == null || yu.equals("")) {
                        yu = answerConstructorService.yuer(gjz4, isDisable, companyId);
                    }
                    if (yu != null && !yu.equals("")) {
                        outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti4, yu)).getString("xiangsijieguo");
                        if (outputText == null || outputText.equals("")) {
                            yu = "";
                        }
                    }
                    if (outputText.equals("")) {
                        String str_c2 = changjingjs(wenti4, C2);
                        yu = answerConstructorService.yusan(gjz4, str_c2, isDisable, companyId);
                        if (yu == null || yu.equals("")) {
                            yu = answerConstructorService.yusan(gjz4, str_c2, isDisable, companyId);
                        }
                        if (yu != null && !yu.equals("")) {
                            outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti4, yu)).getString("xiangsijieguo");
                            if (isEmpty(outputText)) {
                                yu = "";
                            } else {
                                chatCache.setScene(C2);
                            }
                        }
                    }
                }
                if (!outputText.equals("")) {
                    wenti1 = wenti4;
                }
            }
        }

//        if (outputText.equals("") && !gjz1.equals("")) {
//            outputText = tuili(gjz1, isDisable, userId);
//            if (outputText.equals("")) {
//                outputText = tuili1(gjz1, isDisable, userId);
//            }
//            if (outputText.equals("")) {
//                outputText = tuili2(gjz1, isDisable, userId);
//            }
//        }

        int i = (int) Math.rint(Math.random() * 40);
        if (outputText.equals("") && i > 33) {
            if ((!a0[0].equals("") || !a0[1].equals(""))) {
//                String bin = binyujs(gjz1);
//                G7 = G7.replace(zhuyujs(G7), "");
//                wenti7 = wendabuquan(bin, G7);
                if (yu == null || yu.equals("")) {
                    yu = answerConstructorService.yuer(gjz1, isDisable, companyId);
                }
                if (yu != null && !yu.equals("")) {
                    outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti7, yu)).getString("xiangsijieguo");
                    if (outputText == null || outputText.equals("")) {
                        yu = "";
                    }
                }
                wenti1 = R2;
            }
        }

        //第十四种情况: 模糊处理
        if ((outputText == null || outputText.equals(""))) {
            if (yu == null || yu.equals("")) {
                yu = answerConstructorService.yusan(gjz1, C2, isDisable, companyId);
            }
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }

        }
        //第十五种情况: 变换场景的模糊处理
        if ((outputText == null || outputText == "")) {
            if (yu == null || yu.equals("")) {
                yu = answerConstructorService.yusan(gjz1, C5, isDisable, companyId);
            }
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }

        String[] str_subject = gjz1.split(",");
        //第八 B库省略主语的模糊查询（不计算场景，kw1="你"，查kw1,kw2,kw3,kw4）
        if (outputText.equals("") && str_subject[0].equals("k$z")) {
            str_subject[0] = "你";
            String str_array = Arrays.toString(str_subject).replace("[", "").replace("]", "");
            str_subject[0] = "k$z";
            yu = answerConstructorService.yuzuObject(str_array, isDisable, companyId);
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }
//        {"gjz1":"k$z","gjz2":"机器人","gjz3":"打死","gjz4":"k$z","gjz5":"了"}
        //第九 B库省略主语的模糊查询（不计算场景，kw1="我"，查kw1,kw2,kw3,kw4）
        if (outputText.equals("") && (str_subject[0] == "k$z" || str_subject[0] == "你")) {
            str_subject[0] = "我";
            String str_array = Arrays.toString(str_subject).replace("[", "").replace("]", "");
            str_subject[0] ="k$z";
            yu = answerConstructorService.yuzuObject(str_array, isDisable, companyId);
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }

        //第八 B库省略主语的模糊查询（不计算场景，kw1="你"，查kw1,kw2,kw3,kw4）
        if (outputText.equals("") && str_subject[0].equals("k$z")) {
            str_subject[0] = "你们";
            String str_array = Arrays.toString(str_subject).replace("[", "").replace("]", "");
            str_subject[0] = "k$z";
            yu = answerConstructorService.yuzuObject(str_array, isDisable, companyId);
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }
//        {"gjz1":"k$z","gjz2":"机器人","gjz3":"打死","gjz4":"k$z","gjz5":"了"}
        //第九 B库省略主语的模糊查询（不计算场景，kw1="我"，查kw1,kw2,kw3,kw4）
        if (outputText.equals("") && (str_subject[0] == "k$z" || str_subject[0] == "你")) {
            str_subject[0] = "我们";
            String str_array = Arrays.toString(str_subject).replace("[", "").replace("]", "");
            str_subject[0] ="k$z";
            yu = answerConstructorService.yuzuObject(str_array, isDisable, companyId);
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }

        String[] str_subject1 = gjz1.split(",");
        //第十 B库省略主语的模糊查询（不计算场景，kw1="你"，查kw1,kw2,kw3,kw4）
        if (outputText.equals("") && str_subject1[0].equals("你")) {
            str_subject1[0] = "k$z";
            String str_array = Arrays.toString(str_subject1).replace("[", "").replace("]", "").replace(" ", "");
            yu = answerConstructorService.yuzuObject(str_array, isDisable, companyId);
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }
        String[] str_subject2 = gjz1.split(",");
//        {"gjz1":"k$z","gjz2":"机器人","gjz3":"打死","gjz4":"k$z","gjz5":"了"}
        //第十一 B库省略主语的模糊查询（不计算场景，kw1="我"，查kw1,kw2,kw3,kw4）
        if (isEmpty(outputText) && str_subject2[0].equals("我")) {
            str_subject2[0] = "k$z";
            String str_array = Arrays.toString(str_subject2).replace("[", "").replace("]", "").replace(" ", "");
            yu = answerConstructorService.yuzuObject(str_array, isDisable, companyId);
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }

        //
        //第十六种情况: B库问题模糊查询（上句场景，问题包含kw1,kw2,kw3,kw4）
        if (isEmpty(outputText)) {
            if (yu == null || yu.equals("")) {
                yu = answerConstructorService.yuzuQuestion(gjz1, C2, isDisable, companyId);
            }
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }

        //第十七种情况: B库问题模糊查询（本句句场景，问题包含kw1,kw2,kw3,kw4）
//        if (isEmpty(outputText)) {
//            if (yu == null || yu.equals("")) {
//                yu = answerConstructorService.yuzuQuestion(gjz1, C5, isDisable, companyId);
//            }
//            if (yu != null && !yu.equals("")) {
//                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
//                if (outputText == null || outputText.equals("")) {
//                    yu = "";
//                }
//            }
//        }
        //第十七种情况: B库问题模糊查询（上句场景，问题包含kw1,kw2,kw3）
        if (isEmpty(outputText)) {
            if (yu == null || yu.equals("")) {
                yu = answerConstructorService.yuerQuestion(gjz1, C2, isDisable, companyId);
            }
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }
        //第十七种情况: B库问题模糊查询（本句句场景，问题包含kw1,kw2,kw3,kw4）
//        if (isEmpty(outputText)) {
//            if (yu == null || yu.equals("")) {
//                yu = answerConstructorService.yuerQuestion(gjz1, C5, isDisable, companyId);
//            }
//            if (yu != null && !yu.equals("")) {
//                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
//                if (outputText == null || outputText.equals("")) {
//                    yu = "";
//                }
//            }
//        }

        //第十六 无场景算法
        if (outputText.equals("")) {
            yu = answerConstructorService.yuzu(gjz1, isDisable, companyId);
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }

        //第十七 无场景算法
        if (outputText.equals("")) {
            yu = answerConstructorService.yuzuObject(gjz1, isDisable, companyId);
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }

        //第十八 无场景算法
        if (outputText.equals("")) {
            yu = answerConstructorService.yusanObject(gjz1, isDisable, companyId);
            if (yu != null && !yu.equals("")) {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }
        //第十九 B库答案模糊查询
        if (isEmpty(outputText)) {
            yu = answerConstructorService.yuzuAnswer(gjz1, C2, isDisable, companyId);
            if (isEmpty(yu)) {
                yu = answerConstructorService.yuzuObjectAnswer(gjz1, C2, isDisable, companyId);
            }
            if (isEmpty(yu)) {
                yu = answerConstructorService.yusanOblectAnswer(gjz1, C2, isDisable, companyId);
            }
            if (!isEmpty(yu)) {
                String[] strings = yu.split("fen!ge");
                int n = strings.length - 1;
                int m = (int) (Math.random() * n);
                outputText = strings[m];
            }
        }
        //第二十 B库答案模糊查询
        if (isEmpty(outputText)) {
            yu = answerConstructorService.yuzuAnswer(gjz1, C5, isDisable, companyId);
            if (isEmpty(yu)) {
                yu = answerConstructorService.yuzuObjectAnswer(gjz1, C5, isDisable, companyId);
            }
            if (isEmpty(yu)) {
                yu = answerConstructorService.yusanOblectAnswer(gjz1, C5, isDisable, companyId);
            }
            if (!isEmpty(yu)) {
                String[] strings = yu.split("fen!ge");
                int n = strings.length - 1;
                int m = (int) (Math.random() * n);
                outputText = strings[m];
            }
        }


        String result = "";
        String daan = "";
        String[] str_output = null;
        if (outputText.contains("|")) {
            str_output = outputText.split("\\|");
            chatCache.setAvailable("1");//1表示答案无效,0表示答案有效。
            map.put("enabled", "1");
            map.put("scene", null);
        } else {
            str_output = outputText.split(",");
            if (str_output != null && str_output.length >= 4) {
                chatCache.setScene(str_output[3]);
                chatCache.setAvailable("0");//1表示答案无效,0表示答案有效。
                map.put("enabled", "0");
                map.put("scene", str_output[3]);
                map.put("id", str_output[str_output.length - 1]);
            }
        }
        if (str_output != null && str_output.length >= 2) {
            if (str_output[1].contains("/") && str_output.length < 3) {
                daan = str_output[0];
            } else {
                daan = str_output[1];
            }
        }
        if (!daan.equals("") && !daan.substring(daan.length() - 1).equals("!") && !daan.substring(daan.length() - 1).equals("?") && !daan.substring(daan.length() - 1).equals(".") && !daan.substring(daan.length() - 1).equals("！") && !daan.substring(daan.length() - 1).equals("。") && !daan.substring(daan.length() - 1).equals("？")) {
            daan = daan + "。";
        }
        chatCache.setAnswer(daan);
        chatCache.setQieci(T);//切词into Cache
        if (chatList == null) {
            chatList = new ArrayList<>();
        }
        if (!isEmpty(daan) && chatInfo.getUuid() != null) {
            chatList.add(chatCache);
            Date date2 = Utils.now();
            if (chatList.size() > 3) {
                List<Chat> chats = new ArrayList<>();
                redisCache.del(chatInfo.getUuid());
                chats.add(chatList.get(chatList.size() - 3));
                chats.add(chatList.get(chatList.size() - 2));
                chats.add(chatList.get(chatList.size() - 1));
                redisCache.put(chatInfo.getUuid(), chats);
            } else {
                redisCache.put(chatInfo.getUuid(), chatList);
            }
        }
        result = daan;
        map.put("question", bencishuru);
        map.put("answer", result);
        map.put("resouce", "企业知识库");
        return map;
    }

    @Override
    public Map<String, String> tuilijiByOriginalProblem(Chat chatInfo, String C2, String N2, String R2, String G2, String D2, String G3, String R3) {
        String bencishuru = chatInfo.getQuestion();
        Map<String, String> map = new HashMap<>();
        Chat chatCache = new Chat();//用于存放聊天缓存
        String isDisable = chatInfo.getAvailable();
        List<Chat> chatList = new ArrayList<>();
        if (chatInfo.getUuid() != null) {
            Date date = Utils.now();
            chatList = redisCache.get(chatInfo.getUuid());
        }
        chatCache.setUuid(chatInfo.getUuid());
        chatCache.setQuestion(chatInfo.getQuestion());
        String companyId = String.valueOf(chatInfo.getCompanyId());
        if (companyId == null || companyId.equals("null")) {
            Account account = accountMapper.getByMobile(chatInfo.getUsername());
            if (null == account) {
                companyId = "-1";
            } else {
                companyId = String.valueOf(account.getCompanyId());
            }
        }
        if (chatList != null && chatList.size() > 0 && bencishuru != null && bencishuru.equals("继续")) {
            //从缓存里获取上句问题
            for (int i = chatList.size() - 1; i >= 0; i--) {
                if (!chatList.get(i).getQuestion().equals("继续")) {
                    bencishuru = chatList.get(i).getQuestion();
                    break;
                }
            }
        }
        //同义算法 B库企业同义词处理（把用户输入切词后做企业同义词替换，知识库问题不做企业同义词替换）
        String key = "CustomerSynonym" + chatInfo.getUsername();
        List<CustomerSynonym> customerSynonymList = new ArrayList<>();
        customerSynonymList = redisCache.get(key);
        if (null != customerSynonymList && customerSynonymList.size() > 0) {
            for (int j = 0; j < customerSynonymList.size(); j++) {
                if (bencishuru.contains(customerSynonymList.get(j).getWord_old())) {
                    bencishuru = bencishuru.replace(customerSynonymList.get(j).getWord_old(), customerSynonymList.get(j).getWord_new());
                }
            }
        }
        //获取本次用户输入，切词后
        Map<String, String> remote_map = new HashMap<>();
        remote_map.put("question", bencishuru);
        remote_map.put("scene", C2);
        String qieci_result = SemanticsUtil.getRequest1(input_url, remote_map);
        String T = JSON.parseObject(qieci_result).getString("qieci");
        remote_map.remove("question");

        String outputText = "";

        String R4 = "";
        String G4 = "";
        String yu = "";
        String gjz1 = "";
        String gjz3 = "";
        String wenti3 = "";
        String gjz4 = "";
        String wenti4 = "";
        String gjz5 = "";
        String wenti5 = "";
        String C5 = "";

        String gjz7 = "";
        String wenti7 = "";

        if ((C2 == null || C2.isEmpty()) && T.equals(""))//当本剧输入为空并且上句场景为空，这里设定默认为题为“你好”
        {
            T = "你好";
        }
        String wenti1 = T;
        String wenti100 = "";

        wenti100 = wenti1;
        //这里计算本句关键字
        remote_map.put("keyword", wenti1);
        String keywords = SemanticsUtil.getRequest1(keyword_url, remote_map);
        JSONObject jsonObject = JSON.parseObject(keywords);
        String new_kw1 = jsonObject.getString("gjz1");
        String new_kw2 = jsonObject.getString("gjz2");
        String new_kw3 = jsonObject.getString("gjz3");
        String new_kw4 = jsonObject.getString("gjz4");
        String new_kw5 = jsonObject.getString("gjz5");
        remote_map.remove("keyword");
        gjz1 = new_kw1 + "," + new_kw2 + "," + new_kw3 + "," + new_kw4 + "," + new_kw5;
        chatCache.setKeywords(gjz1);

        C5 = changjingjs(wenti1, null);//这里计算本句场景
        chatCache.setScene(C5);
        //整句匹配（用户输入和问题两个去掉标点符号以后做整句匹配）（上句场景）
        if (isEmpty(yu)){
            yu = answerConstructorService.findByQuestionAndScene(bencishuru, C2, isDisable, companyId);
        }
        if (yu != null && !yu.equals("")) {
            outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
            if (outputText == null || outputText.equals("")) {
                yu = "";
            }
        }

        if ((outputText == null || outputText == "")) {
            yu = answerConstructorService.findByQuestionAndScene(bencishuru, C5, isDisable, companyId);
            if (yu != null && yu != "") {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }
        //整句匹配（用户输入和问题两个去掉标点符号以后做整句匹配）（上句场景）
        if (isEmpty(yu)){
            yu = answerConstructorService.findByHandleQuestionAndScene(T.replace(" ",""), C2, isDisable, companyId);
        }
        if (yu != null && !yu.equals("")) {
            outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
            if (outputText == null || outputText.equals("")) {
                yu = "";
            }
        }

        if ((outputText == null || outputText == "")) {
            yu = answerConstructorService.findByHandleQuestionAndScene(T.replace(" ",""), C5, isDisable, companyId);
            if (yu != null && yu != "") {
                outputText = JSON.parseObject(semanticsService.semanticMyopia(wenti1, yu)).getString("xiangsijieguo");
                if (outputText == null || outputText.equals("")) {
                    yu = "";
                }
            }
        }
        String result = "";
        String daan = "";
        String[] str_output = null;
        if (outputText.contains("|")) {
            str_output = outputText.split("\\|");
            chatCache.setAvailable("1");//1表示答案无效,0表示答案有效。
            map.put("enabled", "1");
            map.put("scene", null);
        } else {
            str_output = outputText.split(",");
            if (str_output != null && str_output.length >= 4) {
                chatCache.setScene(str_output[3]);
                chatCache.setAvailable("0");//1表示答案无效,0表示答案有效。
                map.put("enabled", "0");
                map.put("scene", str_output[3]);
                map.put("id", str_output[str_output.length - 1]);
            }
        }
        if (str_output != null && str_output.length >= 2) {
            if (str_output[1].contains("/") && str_output.length < 3) {
                daan = str_output[0];
            } else {
                daan = str_output[1];
            }
        }
        if (!daan.equals("") && !daan.substring(daan.length() - 1).equals("!") && !daan.substring(daan.length() - 1).equals("?") && !daan.substring(daan.length() - 1).equals(".") && !daan.substring(daan.length() - 1).equals("！") && !daan.substring(daan.length() - 1).equals("。") && !daan.substring(daan.length() - 1).equals("？")) {
            daan = daan + "。";
        }
        chatCache.setAnswer(daan);
        chatCache.setQieci(T);//切词into Cache
        if (chatList == null) {
            chatList = new ArrayList<>();
        }
        if (!isEmpty(daan) && chatInfo.getUuid() != null) {
            chatList.add(chatCache);
            Date date2 = Utils.now();
            if (chatList.size() > 3) {
                List<Chat> chats = new ArrayList<>();
                redisCache.del(chatInfo.getUuid());
                chats.add(chatList.get(chatList.size() - 3));
                chats.add(chatList.get(chatList.size() - 2));
                chats.add(chatList.get(chatList.size() - 1));
                redisCache.put(chatInfo.getUuid(), chats);
            } else {
                redisCache.put(chatInfo.getUuid(), chatList);
            }
        }
        result = daan;
        map.put("question", bencishuru);
        map.put("answer", result);
        map.put("resouce", "企业知识库");
        return map;
    }

    public String shengluebuquan(String aj, String bj) {
        String jieguo = "";

        String gb[] = bj.split(",");//关键字分析
        String ga[] = aj.split(",");//关键字分析
        for (int g = 0; g < 5; g++) {
            if (!ga[g].equals("k$z")) {
                jieguo = jieguo + " " + ga[g];
            } else {
                if (!gb[g].equals("k$z")) {
                    jieguo = jieguo + " " + gb[g];
                }
            }

        }
        jieguo = jieguo.trim();
        return jieguo;
    }

    public double shengluepanduan(String a, String b) {
        // TODO 自动生成的方法存根
        double jieguo = 0;
        String ga[] = a.split(",");//关键字分析
        String gb[] = b.split(",");//关键字分析
        for (int ab = 0; ab < ga.length; ab++) {
            if ((ga[0].equals("k$z") && gb[0].equals("k$z")) || (!ga[0].equals("k$z") && !gb[0].equals("k$z"))) {
                jieguo = jieguo + 1;
            }
        }

        jieguo = jieguo / ga.length;
        return jieguo;
    }

    public boolean shengluepanduan1(String a, String b) {
        // TODO 自动生成的方法存根
        boolean jieguo = false;
        String ga[] = a.split(",");//关键字分析
        String gb[] = b.split(",");//关键字分析
        for (int aa = 0; aa < 4; aa++) {
            if (ga[aa].equals(gb[aa])) {
                jieguo = true;
            } else {
                if (ga[aa].equals("k$z")) {
                    jieguo = true;
                } else {
                    if (aa == 1 && !gb[aa].equals("k$z")) {
                        jieguo = true;
                    } else {
                        jieguo = false;
                        break;
                    }
                }
            }
        }
        return jieguo;
    }

    public String wendabuquan(String wenti, String dg1) {
        // TODO 自动生成的方法存根
        String jieguo1 = "";
        jieguo1 = wenti + dg1;
        jieguo1 = jieguo1.replace(",", "");
        jieguo1 = jieguo1.replace("k$z", "");
        jieguo1 = jieguo1.replace(" ", "");
        //jieguo1 = Yuyijs.shuruchuli(jieguo1);
        jieguo1 = JSON.parseObject(semanticsService.inputHandling(jieguo1, null)).getString("qieci");
        //jieguo1 = Yuyijs.guanjianzijs(jieguo1);
        JSONObject jsonObject = JSON.parseObject(semanticsService.keywordProcessing(jieguo1));
        StringBuffer stringBuffer = new StringBuffer();
        for (int f = 1; f <= jsonObject.size(); f++) {
            stringBuffer.append(new StringBuffer(jsonObject.getString("gjz" + f) + ","));
        }
        jieguo1 = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
        jieguo1 = jieguo1.replace(",", "");
        jieguo1 = jieguo1.replace("k$z", "");
        //jieguo1 = Yuyijs.shuruchuli(jieguo1);
        jieguo1 = JSON.parseObject(semanticsService.inputHandling(jieguo1, null)).getString("qieci");
        return jieguo1;
    }

    public boolean xiangsipd(String d1, String d2) {
        boolean bbb = false;
        String g1[] = d1.split(",");
        String g2[] = d2.split(",");
        for (int i = 0; i < 4; i++) {
            if (g1[i] == g2[i] && !g2[i].equals("k$z")) {
                bbb = true;
                break;
            }
        }
        return bbb;
    }

    public boolean shangjvpd(String d2) {
        boolean bbb = false;
        String g2[] = d2.split(",");
        if (!g2[0].equals("k$z") || !g2[1].equals("k$z") && !g2[2].equals("k$z")) {
            bbb = true;
        } else {
            bbb = false;
        }
        return bbb;
    }

    public String shangjvgjzjs(String d1) {
        String bbb = "";
        String[] ccc1 = new String[]{
                "。",
                "？",
                "！"};

        String d2 = "";
        for (int in = 0; in < ccc1.length; in++) {
            d1 = d1.replace(ccc1[in], "as1df");

        }
        d1 = d1.trim();
        if (d1.length() > 0 && d1.substring(d1.length() - 1).equals("as1df")) {
            d1 = d1.substring(0, d1.length() - 1);
        }
        if (d1.indexOf("as1df") > -1) {
            String ccc2[] = d1.split("as1df");
            bbb = ccc2[ccc2.length - 1];
        } else {
            bbb = d1;
        }
        if (!bbb.equals("")) {
            d2 = bbb.replace("你", "wo1");
            d2 = d2.replace("我", "ni1");
            d2 = d2.replace("wo1", "我");
            d2 = d2.replace("ni1", "你");
            //d2=Yuyijs.shuruchuli(d2);
            d2 = JSON.parseObject(semanticsService.inputHandling(d2, null)).getString("qieci");
            //d2=Yuyijs.guanjianzijs(d2);
            JSONObject jsonObject = JSON.parseObject(semanticsService.keywordProcessing(d2));
            StringBuffer stringBuffer = new StringBuffer();
            for (int f = 1; f <= jsonObject.size(); f++) {
                stringBuffer.append(new StringBuffer(jsonObject.getString("gjz" + f) + ","));
            }
            d2 = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
        }
        return d2;
    }

    public String daichichuli(String wenti1, String a10, String a11) {
        // TODO 自动生成的方法存根
        if (wenti1.indexOf("他 ") > -1 || wenti1.indexOf("她 ") > -1 || wenti1.indexOf(" 他") > -1 || wenti1.indexOf(" 她") > -1) {
            if (wenti1.indexOf(a10) > -1) {
                wenti1 = wenti1.replace(a10, "");
            }
            wenti1 = wenti1.replace("他 ", a10 + " ");
            wenti1 = wenti1.replace("她 ", a10 + " ");
            wenti1 = wenti1.replace(" 他", " " + a10);
            wenti1 = wenti1.replace(" 她 ", " " + a10);
        } else if (wenti1.indexOf("它 ") > -1 || wenti1.indexOf(" 它") > -1) {
            if (wenti1.indexOf(a10) > -1) {
                wenti1 = wenti1.replace(a10, "");
            }
            wenti1 = wenti1.replace("它 ", a11 + " ");
            wenti1 = wenti1.replace(" 它", " " + a11);
        } else if (wenti1.indexOf("这 ") > -1 || wenti1.indexOf("那 ") > -1) {
            if (wenti1.indexOf(a11) > -1) {
                wenti1 = wenti1.replace(a11, "");
            }
            wenti1 = wenti1.replace("这 ", a11 + " ");
            wenti1 = wenti1.replace("那 ", a11 + " ");
        }

        return wenti1;
    }

    public String zhuyujs(String gjz1) {
        String g1[] = gjz1.split(",");
        if (!g1[0].equals("k$z") && g1[1].equals("k$z")) {
            gjz1 = g1[0];
        } else if (g1[0].equals("k$z") && !g1[1].equals("k$z")) {
            gjz1 = g1[1];
        } else if (!g1[0].equals("k$z") && !g1[1].equals("k$z")) {
            gjz1 = g1[0] + g1[1];
        } else {
            gjz1 = "";
        }
        return gjz1;
    }

    public String binyujs(String gjz1) {
        String g1[] = gjz1.split(",");
        if (!g1[0].equals("k$z") && !g1[1].equals("k$z")) {
            gjz1 = g1[1];
        } else if (!g1[1].equals("k$z") && g1[0].equals("k$z")) {
            if (Integer.valueOf(semanticsService.wordValue(g1[3], "cixing")) < 3) {
                gjz1 = g1[3];
            } else if (Integer.valueOf(semanticsService.wordValue(g1[4], "cixing")) < 3) {
                gjz1 = g1[4];
            } else {
                gjz1 = "";
            }
        } else {
            gjz1 = "";
        }
        return gjz1;
    }

    public String bypaixu(String a) {
        String da = "";
        String jilus[] = a.split("fen!ge");
        String jilupx = "";
        String ps = "";
        String psg = "";
        String psb = "";
        int pbyz = 0;
        for (int lk = 0; lk < jilus.length; lk++) {
            ps = jilus[lk];
            //psg = Yuyijs.shuruchuli(ps);
            psg = JSON.parseObject(semanticsService.inputHandling(ps, null)).getString("qieci");
            //psg = Yuyijs.guanjianzijs(psg);
            JSONObject jsonObject = JSON.parseObject(semanticsService.keywordProcessing(psg));
            StringBuffer stringBuffer = new StringBuffer();
            for (int f = 1; f <= jsonObject.size(); f++) {
                stringBuffer.append(new StringBuffer(jsonObject.getString("gjz" + f) + ","));
            }
            psg = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
            psb = binyujs(psg);
            pbyz = Integer.valueOf(semanticsService.wordValue(psb, "zhi"));
            jilupx = jilupx + "f！g" + ps + "," + pbyz;
        }
        jilupx = jilupx.substring(3);
        jilupx = jilupx.trim();
        if (!jilupx.equals("")) {
            String[] jilupx1 = jilupx.split("f！g");
            int niu = 0;
            int niu1 = 0;
            int niu2 = 200;
            for (int px = 0; px < jilupx1.length; px++) {
                String[] chongxin = jilupx1[px].split(",");
                niu = Integer.parseInt(chongxin[1]);
                if (niu > niu1) {
                    niu1 = niu;
                }
            }
            for (int px = 0; px < jilupx1.length; px++) {
                String[] chongxin = jilupx1[px].split(",");
                niu = Integer.parseInt(chongxin[1]);
                if (niu < niu2) {
                    niu2 = niu;
                }
            }
            niu = niu1;
            while (niu > niu2 - 1) {

                for (int px = 0; px < jilupx1.length; px++) {
                    String[] chongxin = jilupx1[px].split(",");
                    int niu3 = Integer.parseInt(chongxin[1]);
                    if (niu3 == niu) {
                        da = da + "fen!ge" + chongxin[0];

                    }
                }
                niu--;
            }

        }
        return da;
    }

    public String suijidaan() {
        String da = "";
        int i = (int) Math.rint(Math.random() * 40);
        switch (i) {
            case 2:
                da = "也许我们可以聊点别的！比如，讲笑话？|5/";
                break;
            case 3:
                da = "要不我讲笑话给你听？可以跟我说“讲笑话”。|2/";
                break;
            case 10:
                da = "跟我说“播放音乐”看看。|2/";
                break;
            case 11:
                da = "这是什么意思？能换个说法吗？。|1/";
                break;
            case 12:
                da = "也许你换个说法我就懂了。|5/";
                break;
            case 14:
                da = "抱歉！也许你再说一遍我就听清了。|1/";
                break;
            case 15:
                da = "我是真的没听懂！也许你能换个说法。|1/";
                break;
            case 16:
                da = "不懂你在说什么？能不能换个说法！|2/";
                break;
            case 22:
                da = "我还是个小朋友，懂的不多。能不能换个说法？|9/";
                break;
            case 23:
                da = "我快被你搞崩溃了！|9/";
                break;
            case 24:
                da = "你想不想看我哭！我快被你搞哭了！|9/";
                break;
            case 27:
                da = "你是在故意为难我吗？我还是个孩子！|9/";
                break;
            default:
                da = "你能说清楚点吗？|2/";
        }
        return da;
    }

    public String suijiwenti() {
        String da = "";
        int i = (int) Math.rint(Math.random() * 40);
        switch (i) {
            case 1:
                da = "你是男生女生";
                break;
            case 2:
                da = "你好";
                break;
            case 3:
                da = "讲笑话";
                break;
            case 4:
                da = "性格内向的男生怎样找女朋友";
                break;
            case 5:
                da = "我喜欢你";
                break;
            case 6:
                da = "你结婚没";
                break;
            case 7:
                da = "你从哪里来";
                break;
            case 8:
                da = "你有几个男朋友";
                break;
            case 9:
                da = "你几岁了";
                break;
            case 10:
                da = "你多大了";
                break;
            case 11:
                da = "你是谁";
                break;
            case 12:
                da = "你都会什么";
                break;
            case 13:
                da = "你吃了吗";
                break;
            case 14:
                da = "你漂亮吗";
                break;
            case 15:
                da = "我是男人";
                break;
            case 16:
                da = "做我女朋友好吗";
                break;
            case 17:
                da = "怎么生个宝宝";
                break;
            case 18:
                da = "你妈妈是谁";
                break;
            case 19:
                da = "爸爸的爸爸是谁";
                break;
            case 20:
                da = "打你";
                break;
            case 21:
                da = "你有谈过男朋友吗";
                break;
            case 22:
                da = "你愿做我的女朋友吗";
                break;
            case 23:
                da = "你真笨";
                break;
            case 24:
                da = "你真聪明";
                break;
            case 25:
                da = "你是个傻子";
                break;
            case 26:
                da = "我讨厌你";
                break;
            case 27:
                da = "可以做我女友吗";
                break;
            case 30:
                da = "你有小三吗";
                break;
            default:
                da = "细痣疣螈是什么";

        }
        return da;
    }

    public String suijifanwen() {
        String da = "";
        int i = (int) Math.rint(Math.random() * 100);
        switch (i) {
            case 1:
                da = "要放音乐吗？";
                break;
            case 2:
                da = "要讲笑话吗？";
                break;
            case 3:
                da = "要看电视吗？";
                break;
            case 4:
                da = "你是女生吗？";
                break;
            case 5:
                da = "你是男生吗？";
                break;
            case 6:
                da = "你是中国人吗？";
                break;
            case 7:
                da = "你是机器人吗？";
                break;
            case 8:
                da = "什么动物喜欢吃鱼？";
                break;
            case 9:
                da = "什么动物喜欢吃老鼠？";
                break;
            case 10:
                da = "什么动物喜欢吃羊？";
                break;
            case 11:
                da = "什么动物喜欢吃骨头？";
                break;
            case 12:
                da = "什么动物喜欢吃谷物？";
                break;
            case 13:
                da = "什么动物喜欢吃草？";
                break;
            case 14:
                da = "什么动物喜欢吃虫子？";
                break;
            default:
                da = "什么动物喜欢吃骨头？";

        }
        return da;
    }

    public boolean isNum(String str) {

        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
        // TODO 自动生成的方法存根

    }


    public String guanjianzibuquan(String a, String b) {
        String jieguo = "";
        String ga[] = a.split(",");//关键字分析
        String gb[] = b.split(",");//关键字分析
        if (ga[0].equals("k$z") && !gb[0].equals("k$z")) {
            a = gb[0];
        } else {
            a = ga[0];
        }
        if (ga[1].equals("k$z") && !gb[1].equals("k$z")) {
            a = a + "," + gb[1];
        } else {
            a = a + "," + ga[1];
        }
        if (ga[2].equals("k$z") && !gb[2].equals("k$z")) {
            a = a + "," + gb[2];
        } else {
            a = a + "," + ga[2];
        }
        a = a + "," + ga[3] + "," + ga[4];
        jieguo = a;
        return jieguo;
    }

    public String buquanjvzi(String aj, String bj, String ag, String bg, int n) {
        String jieguo = "";
        String jieguo1 = aj;
        String ga[] = ag.split(",");//关键字分析
        String gb[] = bg.split(",");//关键字分析
        if (n == 1) {
            if (!gb[1].equals("k$z") && ga[1].equals("k$z")) {
                jieguo1 = gb[1] + " " + jieguo1;
            }
        } else if (n == 0) {
            if (!gb[0].equals("k$z") && ga[0].equals("k$z")) {
                jieguo1 = gb[0] + " " + jieguo1;
            }
        } else if (n == 2) {
            if (!gb[0].equals("k$z") && !gb[1].equals("k$z") && ga[0].equals("k$z")) {
                jieguo1 = gb[0] + " " + gb[1] + " " + jieguo1;
            }
        } else {
            jieguo = jieguo1;
        }
        jieguo = jieguo1;
        jieguo = jieguo.trim();
        return jieguo;
    }

    public String changjingjs(String a, String shang) {
        String cj = shang;
        String a1 = a;
        a = a.trim();
        if (a.indexOf("谁 爱") == 0 || a.indexOf("谁 能") == 0 || a.indexOf("谁 会") == 0) {
            cj = "认识事物";
        } else if (a.indexOf("男朋友") > -1 || a.indexOf("女朋友") > -1) {
            cj = "男女朋友";
        } else if (a.indexOf("你") > -1 && (a.indexOf("干 什么") > -1 || a.indexOf("会 什么") > -1 || a.indexOf("会 做") > -1 || a.indexOf("能 做") > -1 || a.indexOf("会 干") > -1 || a.indexOf("能干") > -1 || a.indexOf("懂") > -1 || a.indexOf("功能") > -1)) {
            cj = "你会什么";
        } else if (a.indexOf("你") > -1 && (a.indexOf("爷") > -1 || a.indexOf("奶") > -1 || a.indexOf("哥") > -1 || a.indexOf("姐") > -1 || a.indexOf("弟") > -1 || a.indexOf("妹") > -1 || a.indexOf("爸") > -1 || a.indexOf("妈") > -1)) {
            cj = "你家庭";
        } else if (a.indexOf("你") > -1 && (a.indexOf("性别") > -1 || a.indexOf("男") > -1 || a.indexOf("女") > -1 || (a.indexOf("公") > -1 && a.indexOf("公") < -1) || a.indexOf("母") > -1)) {
            cj = "你性别";
        } else if (a.indexOf("你") > -1 && (a.indexOf("生 的") > -1 || a.indexOf("出生") > -1 || a.indexOf("年龄") > -1 || a.indexOf("多少 岁") > -1 || a.indexOf("多 大") > -1 || a.indexOf("生日") > -1)) {
            cj = "你年龄";
        } else if ((a.indexOf("你") > -1 && a.indexOf("我") > -1) || a.indexOf("我们") > -1) {
            cj = "我你";
        } else if (a.indexOf("歌") > -1 || a.indexOf("唱") > -1 || a.indexOf("音乐") > -1 || a.indexOf("播放") > -1) {
            cj = "音乐播放";
        } else if (a.indexOf("频道") > -1 || a.indexOf("卫视") > -1 || (a.indexOf("中央 ") > -1 && a.indexOf("套 ") > -1) || a.indexOf("电视") > -1 || a.indexOf("画面") > -1 || a.indexOf("有线") > -1) {
            cj = "看电视";
        } else if (a.indexOf("你 叫 什么") > -1 || a.indexOf("你 是") > -1) {
            cj = "你是";
//        } else if (a.indexOf("早上 好") > -1 || a.indexOf("晚上 好") > -1 || a.indexOf("中午 好") > -1 || a.indexOf("早安") > -1 || a.indexOf("午安") > -1 || a.indexOf("晚安") > -1 || a.equals("嗨") || a.equals("哈喽") || a.equals("你 好") || a.equals("你 好 么") || a.equals("你 好 啊") || a.equals("你 好 吧") || a.equals("你 好 呀") || a.equals("你 好 吗") || a.equals("你 好 不") || a.equals("你 好不好") || (a.indexOf("见到 你") > -1 && ((a.indexOf("开心") > -1) || a.indexOf("高兴") > -1))) {
//            cj = "打招呼";
        } else if (a.indexOf("你") == 0 && a.indexOf("爱") > -1) {
            cj = "你喜欢";
        } else if (a.contains("前进") || a.contains("朝前") || a.equals("前") || a.contains("往前") || a.contains("向前") || a.equals("后") || a.contains("朝后") || a.equals("左") || a.contains("朝左") || a.contains("往左") || a.equals("右") || a.contains("朝右") || a.contains("往右") || a.contains("下 一个") || a.contains("上 一个") || a.contains("停息") || a.contains("暂停") || a.contains("停下") || a.contains("停止") || a.contains("关闭") || a.contains("打开") || a.contains("退出")
                || a.contains("前往") || a.contains("刷新") || a.contains("运动") || a.contains("运动") || a.equals("走") || a.equals("前景") || a.equals("天津") || a.equals("沉浸") || a.equals("前边") || a.contains("滚") || a.contains("滚开") || a.contains("撤退") || a.equals("后腿") || a.equals("后腿") || a.contains("向 后") || a.equals("后边")
                || a.equals("是") || a.equals("确认") || a.equals("好") || a.equals("对") || a.equals("行") || a.equalsIgnoreCase("okey") || a.equalsIgnoreCase("ok") || a.equals("好 吧") || a.equals("是 的") || a.equals("能够") || a.equals("否") || a.equals("取消") || a.equals("算 了") || a.equals("不 是") || a.equals("错误") || a.equals("不 好") || a.equals("不要") || a.equals("不行") || a.equalsIgnoreCase("no") || a.equalsIgnoreCase("cancel") || a.equals("清除") || a.equals("清空") || a.contains("大点 声")
                || a.contains("高声 点") || a.contains("小点 声") || a.contains("小声点") || a.contains("向左") || a.contains("左转") || a.contains("左边") || a.contains("左侧") || a.contains("向 右") || a.contains("右转") || a.contains("右边") || a.contains("右侧")) {
            cj = "运动";
        } else if (a.contains("现在 几 点 了") || a.contains("现在 几 点") || a.contains("几 点 了") || a.contains("几 点 啦") || a.indexOf("星期 几") > -1 || a.equals("时间") || a.indexOf("现在时 间") > -1 || a.indexOf("现在 时间") > -1 || a.indexOf("几 月") > -1 || a.indexOf("几 号") > -1 || a.equals("今天 什么 日子")) {
            cj = "时间";
        } else if ((a.indexOf("定") > -1 && a.indexOf("车票") > -1) || (a.indexOf("订") > -1 && a.indexOf("车票") > -1) || a.equals("火车票") || a.equals("车票")) {
            cj = "订车票";
        } else if (a.equals("新闻") || a.contains("读 新闻") || a.contains("播 新闻") || a.contains("读 个 新闻") || a.contains("播 个 新闻") || a.contains("来 个 新闻") || a.contains("看 新闻")) {
            cj = "新闻";
        } else if (a.equals("故事") || a.contains("讲 故事") || a.contains("听 故事") || a.contains("听 个 故事") || a.contains("讲 个 故事") || a.contains("来 故事") || a.contains("来 个 故事") || a.contains("有 故事") || a.contains("小 故事")) {
            cj = "讲故事";
        } else if (a.equals("笑话") || a.contains("讲 笑话") || a.contains("听 笑话") || a.contains("听 个 笑话") || a.contains("讲 个 笑话") || a.contains("来 笑话") || a.contains("来 个 笑话") || a.contains("有 笑话") || a.contains("小 笑话") || a.contains("个 段子") | a.contains("讲 段子")) {
            cj = "讲笑话";
        } else if (a.equals("酒店") || a.contains("订 酒店") || a.contains("订 个 酒店") || a.contains("预订 酒店") || a.contains("预订 个 酒店") || a.contains("定 酒店") || a.contains("订 个  酒店") || a.contains("预定 酒店") || a.contains("预定 个 酒店")) {
            cj = "酒店";
        } else if (a.contains("天气") || a.contains("气象") || a.contains("需要 带 伞") || a.contains("天 冷不冷") || a.contains("会不会 下雪")) {
            cj = "天气";
        } else if (a.contains("添加 问题")) {
            cj = "添加问题";
        } else if ((a.indexOf("比") > -1 || a.indexOf("谁") > -1 || a.indexOf("和") > -1 || a.indexOf("哪 个") > -1 || a.indexOf(" 大于") > -1 || a.indexOf(" 小于") > -1) && (a.indexOf("大") > -1 || a.indexOf("小") > -1)) {
            cj = "比大小";
        } else if (a.indexOf("加") > -1 || a.indexOf("减") > -1 || a.indexOf("乘") > -1 || a.equals("除") || a.indexOf("取 余") > -1) {
            cj = "计算";
        } else if (a.indexOf("股票") > -1) {
            cj = "股票";
        } else if (a.indexOf("查找") > -1) {
            cj = "查找";
        } else if (a.indexOf("找") > -1 || a.indexOf("面试") > -1 || a.indexOf("口试") > -1 || a.indexOf("快递") > -1 || a.indexOf("参观") > -1 || a.indexOf("观赏") > -1 || a.indexOf("入 职") > -1 || a.indexOf("开会") > -1 || a.indexOf("闭会") > -1) {
            cj = "找人";
        } else if (a.contains("星座") || a.contains("白羊座") || a.contains("金牛座") || a.contains("双子座") || a.contains("巨蟹座") || a.contains("狮子座") || a.contains("处女座") || a.contains("天秤座") || a.contains("天蝎座") || a.contains("射手座") || a.contains("摩羯座") || a.contains("水瓶座") || a.contains("双鱼座")) {
            cj = "星座";
        } else if (a.contains("美眉") || a.contains("美女") || a.contains("girl") || a.contains("妹子") || a.contains("妞")) {
            cj = "美女";
        } else if (a.contains("看图 猜 字")) {
            cj = "看图猜字";
        } else if (a.contains("猜 灯谜")) {
            cj = "猜灯谜";
        } else if (a.contains("我 是 岛主")) {
            cj = "我是岛主";
        } else if (a.contains("学 狗 叫")) {
            cj = "学狗叫";
        } else if (a.contains("翻译")) {
            cj = "翻译";
        } else if (a.equals("安图 声 送 书") || a.equals("安图 声") || a.equals("送 书 举动") || a.equals("送 书 使用") || a.equals("我 想送 书") || a.equals("我 要 送 书") || a.equals("送 书")) {
            cj = "送书";
        } else if (a.contains("早上 好") || a.contains("上午 好") || a.contains("晌午 好") || a.contains("中午 好") || a.contains("下午 好") || a.contains("晚上 好")) {
            cj = "问好";
        } else {
            JSONObject jsonObject = JSON.parseObject(semanticsService.keywordProcessing(a1));
//            JSONObject jsonObject = JSON.parseObject(YuyiTest.guanjianzijsJason(a1));
            cj = jsonObject.getString("gjz1") + "," + jsonObject.getString("gjz2") + "," + jsonObject.getString("gjz3") + "," + jsonObject.getString("gjz4") + "," + jsonObject.getString("gjz5");
//            cj = guanjianzijs(a1);
            String c[] = cj.split(",");
            cj = "";
            for (int ii = 0; ii < 3; ii++) {
                if (!c[ii].equals("k$z")) {
                    cj = c[ii];
                    break;
                }
            }
        }

        return cj;
    }

    public String tuili(String gjz1, String isDisable, String userId) {
        String da = "";
        String wenti6 = "";
        String gjz6 = "";
        String yu = "";
        String zh = zhuyujs(gjz1);
        wenti6 = zh + "是一种什么";
//		wenti6=Yuyijs.shuruchuli(wenti6);
        wenti6 = JSON.parseObject(semanticsService.inputHandling(wenti6, null)).getString("qieci");

//		gjz6=Yuyijs.guanjianzijs(wenti6);
        JSONObject jsonObject = JSON.parseObject(semanticsService.keywordProcessing(wenti6));
        StringBuffer stringBuffer = new StringBuffer();
        for (int f = 1; f <= jsonObject.size(); f++) {
            stringBuffer.append(new StringBuffer(jsonObject.getString("gjz" + f) + ","));
        }
        gjz6 = stringBuffer.toString().substring(0, stringBuffer.length() - 1);

        try {
            yu = answerConstructorService.yuzu(gjz6, isDisable, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String zhu6 = "";
        String js6[] = yu.split("fen!ge");
        for (int ji = 0; ji < js6.length; ji++) {
            if (!js6[ji].equals("") && !gjz1.equals("")) {
                zhu6 = JSON.parseObject(semanticsService.inputHandling(js6[ji], null)).getString("qieci");

                jsonObject = JSON.parseObject(semanticsService.keywordProcessing(zhu6));
                stringBuffer = new StringBuffer();
                for (int f = 1; f <= jsonObject.size(); f++) {
                    stringBuffer.append(new StringBuffer(jsonObject.getString("gjz" + f) + ","));
                }
                zhu6 = stringBuffer.toString().substring(0, stringBuffer.length() - 1);

                zhu6 = binyujs(zhu6);
                gjz6 = gjz1.replace(zhuyujs(gjz1), "");
                wenti6 = wendabuquan(zhu6, gjz6);

                jsonObject = JSON.parseObject(semanticsService.keywordProcessing(wenti6));
                stringBuffer = new StringBuffer();
                for (int f = 1; f <= jsonObject.size(); f++) {
                    stringBuffer.append(new StringBuffer(jsonObject.getString("gjz" + f) + ","));
                }
                gjz6 = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
                try {
                    yu = answerConstructorService.yuer(gjz6, isDisable, userId);
                    if (yu != null && !yu.equals("")) {
                        da = JSON.parseObject(semanticsService.semanticMyopia(wenti6, yu)).getString("xiangsijieguo");
                        if (da == null || da.equals("")) {
                            yu = "";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!da.equals("") || ji > 10) {
                break;
            }
        }
        return da;
    }

    public String tuili1(String gjz1, String isDisable, String userId) {
        String da = "";
        String wenti6 = "";
        String gjz6 = "";
        String yu = "";
        String zh = zhuyujs(gjz1);
        wenti6 = zh + "是一种什么";
        wenti6 = JSON.parseObject(semanticsService.inputHandling(wenti6, null)).getString("qieci");

        JSONObject jsonObject = JSON.parseObject(semanticsService.keywordProcessing(wenti6));
        StringBuffer stringBuffer = new StringBuffer();
        for (int f = 1; f <= jsonObject.size(); f++) {
            stringBuffer.append(new StringBuffer(jsonObject.getString("gjz" + f) + ","));
        }
        gjz6 = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
        try {
            yu = answerConstructorService.yuzu(gjz6, isDisable, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String zhu6 = "";
        String js6[] = yu.split("fen!ge");
        for (int ji = 0; ji < js6.length; ji++) {
            if (!js6[ji].equals("") && !gjz1.equals("")) {
                zhu6 = JSON.parseObject(semanticsService.inputHandling(js6[ji], null)).getString("qieci");
                jsonObject = JSON.parseObject(semanticsService.keywordProcessing(zhu6));
                stringBuffer = new StringBuffer();
                for (int f = 1; f <= jsonObject.size(); f++) {
                    stringBuffer.append(new StringBuffer(jsonObject.getString("gjz" + f) + ","));
                }
                zhu6 = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
                zhu6 = binyujs(zhu6);
                gjz6 = gjz1.replace(zh, "");
                wenti6 = wendabuquan(zhu6, gjz6);
                jsonObject = JSON.parseObject(semanticsService.keywordProcessing(wenti6));
                stringBuffer = new StringBuffer();
                for (int f = 1; f <= jsonObject.size(); f++) {
                    stringBuffer.append(new StringBuffer(jsonObject.getString("gjz" + f) + ","));
                }
                gjz6 = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
                da = tuili(gjz6, isDisable, userId);
            }

            if (!da.equals("") || ji > 10) {
                break;
            }
        }
        return da;
    }

    public String tuili2(String gjz1, String isDisable, String userId) {
        String da = "";
        String wenti6 = "";
        String gjz6 = "";
        String yu = "";
        String zh = zhuyujs(gjz1);
        wenti6 = zh + "是一种什么";
        wenti6 = JSON.parseObject(semanticsService.inputHandling(wenti6, null)).getString("qieci");

        JSONObject jsonObject = JSON.parseObject(semanticsService.keywordProcessing(wenti6));
        StringBuffer stringBuffer = new StringBuffer();
        for (int f = 1; f <= jsonObject.size(); f++) {
            stringBuffer.append(new StringBuffer(jsonObject.getString("gjz" + f) + ","));
        }
        gjz6 = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
        try {
            yu = answerConstructorService.yuzu(gjz6, isDisable, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String zhu6 = "";
        String js6[] = yu.split("fen!ge");
        for (int ji = 0; ji < js6.length; ji++) {
            if (!js6[ji].equals("") && !gjz1.equals("")) {
                zhu6 = JSON.parseObject(semanticsService.inputHandling(js6[ji], null)).getString("qieci");
                jsonObject = JSON.parseObject(semanticsService.keywordProcessing(zhu6));
                stringBuffer = new StringBuffer();
                for (int f = 1; f <= jsonObject.size(); f++) {
                    stringBuffer.append(new StringBuffer(jsonObject.getString("gjz" + f) + ","));
                }
                zhu6 = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
                zhu6 = binyujs(zhu6);
                gjz6 = gjz1.replace(zh, "");
                wenti6 = wendabuquan(zhu6, gjz6);
                jsonObject = JSON.parseObject(semanticsService.keywordProcessing(wenti6));
                stringBuffer = new StringBuffer();
                for (int f = 1; f <= jsonObject.size(); f++) {
                    stringBuffer.append(new StringBuffer(jsonObject.getString("gjz" + f) + ","));
                }
                gjz6 = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
                da = tuili1(gjz6, isDisable, userId);
            }

            if (!da.equals("") || ji > 10) {
                break;
            }
        }
        return da;
    }

}
