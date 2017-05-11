package com.diting.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.diting.cache.Cache;
import com.diting.core.Universe;
import com.diting.dao.*;
import com.diting.model.*;
import com.diting.model.mongo.ChatLog;
import com.diting.model.options.ExternalOptions;
import com.diting.sensitivewords.SensitiveWordFilter;
import com.diting.service.*;
import com.diting.util.HessianClient;
import com.diting.util.KnowledgeUtil;
import com.diting.util.Utils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.diting.util.Utils.*;
import static com.diting.util.Utils.str2json;

@SuppressWarnings("ALL")
@Service("chatService")
@Transactional
public class ChatServiceImpl extends HessianClient implements ChatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatServiceImpl.class);

    @Autowired
    private InferenceEngineService inferenceEngineService;

    @Autowired
    private ExternalApplicationService externalApplicationService;

    @Autowired
    @Qualifier("redisCache")
    private Cache redisCache;
    @Autowired
    private InvalidQuestionService invalidQuestionService;
    @Autowired
    private ChatLogMongoService chatLogMongoService;

    @Autowired
    KnowledgeMapper knowledgeMapper;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    RobotMapper robotMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    TeleOtherMapper teleOtherMapper;

    @Autowired
    CompanyService companyService;

    @Override
    public Chat getChat(Chat chat) {
        chat=getApplication(chat);
        if (!isEmpty(chat.getAnswer()))
            return chat;
        Map<String, String> map = new HashMap<>();
        System.out.println("程序开始执行...");
        String answer = null;
        String username = null;
        Integer userId = null;
        Boolean forbiddenEnable = false; //false 不禁用 true 禁用
        if (isEmpty(chat.getUnique_id())) {
            username = Universe.current().getUserName();
            userId = Universe.current().getUserId();
            Account account = accountMapper.get(userId);
            if (null==account){
                chat.setAnswer("The robot does not exist");
                return chat;
            }
            chat.setCompanyId(account.getCompanyId());
            forbiddenEnable = account.getForbiddenEnable();
            Robot robot=robotMapper.getByUserId(userId);
            if (null!=robot){
                chat.setRobotName(robot.getName());
                chat.setEnable(String.valueOf(robot.isEnable()));
                chat.setInvokeEnable(robot.isInvokeEnable());
                chat.setCharge_mode(robot.getChargeMode());
            }
        }
        if (isEmpty(username) && !isEmpty(chat.getUnique_id())) {
            Robot robot = robotMapper.getByUniqueId(chat.getUnique_id());
            Account account = accountMapper.get(robot.getAccountId());
            if (account != null) {
                username = account.getUserName();
            }
            if (account != null) {
                userId = account.getId();
            } else if (robot != null) {
                userId = robot.getAccountId();
                chat.setRobotName(robot.getName());
            }
            forbiddenEnable = account.getForbiddenEnable();
            chat.setCompanyId(account.getCompanyId());
            chat.setEnable(String.valueOf(robot.isEnable()));
            chat.setInvokeEnable(robot.isInvokeEnable());
//            chat.setIntel_calendar(robot.getIntel_calendar());
            chat.setCharge_mode(robot.getChargeMode());
        }
        chat.setUsername(username);
        chat.setAccountId(userId);
        chat.setSource("web");
        //调用应用
        if (!forbiddenEnable) {
            chat.setAvailable("1");
            map=this.getAnswerByOriginalProblem(chat);
            answer = map.get("answer");
            map.put("dataSource", "B");
            if (isEmpty(answer)){
                chat = getInvoke(chat);
                answer = chat.getAnswer();
            }
            if (!isEmpty(answer)) {
                map.put("username", chat.getUsername());
                map.put("question", chat.getQuestion());
                map.put("answer", answer);
                map.put("appKey", chat.getUnique_id());
                map.put("uuid", chat.getUuid());
                map.put("enabled", "0");
                map.put("extra4", "1");
                map.put("dataSource", "应用");
                if (null==Universe.current().getUserName()){
                    answer="提示以下内容：\n" +
                            "您当前的身份是游客，无法调用应用。\n" +
                            "请登录：http://ditingai.com/m/registerTwo";
                    map.put("answer", "提示以下内容：\n" +
                            "您当前的身份是游客，无法调用应用。\n" +
                            "请登录：http://ditingai.com/m/registerTwo");
                }
            }
            //查询企业知识库
            if (isEmpty(answer)) {
                chat.setAvailable("1");
                map = this.getAnswer(chat);
                answer = map.get("answer");
                map.put("dataSource", "B");
            }

            //查询通用知识库
            if (isEmpty(answer) && (isEmpty(chat.getEnable()) || chat.getEnable().equals("false") || chat.getEnable().equals("False"))) {
                chat.setAvailable("0");
                map = this.getAnswer(chat);
                if (!isEmpty(chat.getRobotName())) {
                    answer = map.get("answer").replace("小谛", chat.getRobotName());
                } else {
                    answer = map.get("answer");
                }
                map.put("dataSource", "A");
            }
            if (chat.getFromusername() != null) {
                map.put("extra4", "2");
            } else {
                map.put("extra4", "1");
            }
            //知识库频次
            if (map.containsKey("id") && map.get("id") != null) {
                knowledgeMapper.updateFrequency(Integer.valueOf(map.get("id")));
                String img_url = (null!=knowledgeMapper.get(Integer.valueOf(map.get("id")))?knowledgeMapper.get(Integer.valueOf(map.get("id"))).getImg_url():null);
                if (!isEmpty(img_url)) {
                    chat.setImg_url(img_url);
                } else {
                    chat.setImg_url(null);
                }
            }
            //获取随机答案
            if (isEmpty(answer)) {
                answer = this.getRandomAnswers(chat);
                map.put("dataSource", "随机答案");
                chat.setImg_url(null);
            }
            map.put("answer", answer);
            chat.setAnswer(answer);
            map.put("charge_mode", chat.getCharge_mode());
            insertChatLog(map);
        } else {
            chat.setAnswer(KnowledgeUtil.ARREARAGE_ANSWER);
            chat.setImg_url(null);
        }
        if (!isEmpty(chat.getAnswer()) && chat.getAnswer().contains("http://ok7ouv8bt.bkt.clouddn.com")) {
            chat.setAnswer("");
        } else if (!isEmpty(chat.getAnswer()) && chat.getAnswer().contains("http://oky49sknd.bkt.clouddn.com")) {
            chat.setImg_url(chat.getAnswer());
            chat.setAnswer("限时45秒");
        }

        return chat;
    }

    @Override
    public Chat getRemoteChat(Chat chat) {
        if (isEmpty(chat.getSource())) {
            chat.setSource("remote");
        }
        chat=getApplication(chat);
        if (!isEmpty(chat.getAnswer()))
            return chat;
        Map<String, String> map = new HashMap<>();
        System.out.println("程序开始执行...");
        String answer = null;
        Boolean forbiddenEnable = false; //false 不禁用 true 禁用
        Account account = accountMapper.getByMobile(chat.getUsername());
        if (null != account) {
            Robot robot = robotMapper.getByUserId(account.getId());
            Company company = companyMapper.get(account.getCompanyId());
            chat.setAccountId(account.getId());
            chat.setCompanyId(account.getCompanyId());
            chat.setRobotName(robot.getName());
            chat.setCompanyName(company.getName());
            chat.setEnable(String.valueOf(robot.isEnable()));
            chat.setAnswer1(robot.getInvalidAnswer1());
            chat.setAnswer2(robot.getInvalidAnswer2());
            chat.setAnswer3(robot.getInvalidAnswer3());
            chat.setAnswer4(robot.getInvalidAnswer4());
            chat.setAnswer5(robot.getInvalidAnswer5());
            forbiddenEnable = account.getForbiddenEnable();
//            chat.setIntel_calendar(robot.getIntel_calendar());
            chat.setCharge_mode(robot.getChargeMode());
        }else {
             account=accountMapper.getByAngelId(chat.getUsername());
            if (null==account)
                return chat;
        }

        if (!forbiddenEnable) {
            chat.setAvailable("1");
            map=this.getAnswerByOriginalProblem(chat);
            answer = map.get("answer");
            map.put("dataSource", "B");
            if (isEmpty(answer)){
                chat = getInvoke(chat);
                answer = chat.getAnswer();
            }
            if (!isEmpty(answer)) {
                map.put("username", chat.getUsername());
                map.put("question", chat.getQuestion());
                map.put("answer", answer);
                map.put("appKey", chat.getUnique_id());
                map.put("uuid", chat.getUuid());
                map.put("enabled", "0");
                map.put("extra4", "1");
                map.put("dataSource", "应用");
            }
            if (isEmpty(chat.getQuestion())) {
                chat.setQuestion("");
            }

            //查询企业知识库
            if (isEmpty(answer)) {
                chat.setAvailable("1");
                map = this.getAnswer(chat);
                answer = map.get("answer");
                map.put("dataSource", "B");
            }
            //查询通用知识库
            if (isEmpty(answer) && (isEmpty(chat.getEnable()) || chat.getEnable().equalsIgnoreCase("false"))) {
                chat.setAvailable("0");
                map = this.getAnswer(chat);
                if (!isEmpty(chat.getRobotName())) {
                    answer = map.get("answer").replace("小谛", chat.getRobotName());
                } else {
                    answer = map.get("answer");
                }
                map.put("answer", answer);
                map.put("dataSource", "A");
            }
            if (chat.getFromusername() != null) {
                map.put("extra4", "2");
            } else {
                map.put("extra4", "1");
            }
            //知识库频次
            if (map.containsKey("id") && map.get("id") != null) {
                knowledgeMapper.updateFrequency(Integer.valueOf(map.get("id")));
                Knowledge knowledge = knowledgeMapper.get(Integer.valueOf(map.get("id")));
                String img_url = knowledge.getImg_url();
                if (!isEmpty(img_url)) {
                    chat.setImg_url(img_url);
                } else {
                    chat.setImg_url(null);
                }
                chat.setActionOption(knowledge == null ? "action_0" : knowledge.getActionOption());
            } else {
                chat.setActionOption("action_0");
                chat.setImg_url(null);
            }
            //获取随机答案
            if (isEmpty(answer)) {
                answer = this.getRandomAnswers(chat);
                map.put("answer", answer);
                map.put("dataSource", "随机答案");
                chat.setAvailable("1");
                chat.setImg_url(null);
            } else {
                chat.setAvailable("0");
            }
            chat.setAnswer(answer);
            insertChatLog(map);
        } else {
            chat.setAnswer(KnowledgeUtil.ARREARAGE_ANSWER);
            chat.setImg_url(null);
        }
        if (!isEmpty(chat.getAnswer()) && chat.getAnswer().contains("http://ok7ouv8bt.bkt.clouddn.com")) {
            chat.setAnswer("");
        } else if (!isEmpty(chat.getAnswer()) && chat.getAnswer().contains("http://oky49sknd.bkt.clouddn.com")) {
            chat.setImg_url(chat.getAnswer());
            chat.setAnswer("限时45秒");
        }
        return chat;
    }

    @Override
    public Chat getAngelChat(Chat chat) {
        if (isEmpty(chat.getSource())) {
            chat.setSource("remote");
        }
        Map<String, String> map = new HashMap<>();
        System.out.println("程序开始执行...");
        String answer = null;
        Boolean forbiddenEnable = false; //false 不禁用 true 禁用
        Account account=accountMapper.getByAngelId(chat.getUsername());
        if (null != account) {
            Robot robot = robotMapper.getByUserId(account.getId());
            Company company = companyMapper.get(account.getCompanyId());
            chat.setAccountId(account.getId());
            chat.setCompanyId(account.getCompanyId());
            chat.setRobotName(robot.getName());
            chat.setCompanyName(company.getName());
            chat.setEnable(String.valueOf(robot.isEnable()));
            chat.setAnswer1(robot.getInvalidAnswer1());
            chat.setAnswer2(robot.getInvalidAnswer2());
            chat.setAnswer3(robot.getInvalidAnswer3());
            chat.setAnswer4(robot.getInvalidAnswer4());
            chat.setAnswer5(robot.getInvalidAnswer5());
            forbiddenEnable = account.getForbiddenEnable();
            chat.setCharge_mode(robot.getChargeMode());
        }else {
            return chat;
        }

        if (!forbiddenEnable) {
            //查询企业知识库
            if (isEmpty(answer)) {
                chat.setAvailable("1");
                map = this.getAnswer(chat);
                answer = map.get("answer");
                map.put("dataSource", "B");
            }
            if (chat.getFromusername() != null) {
                map.put("extra4", "2");
            } else {
                map.put("extra4", "1");
            }
            //知识库频次
            if (map.containsKey("id") && map.get("id") != null) {
                knowledgeMapper.updateFrequency(Integer.valueOf(map.get("id")));
                Knowledge knowledge = knowledgeMapper.get(Integer.valueOf(map.get("id")));
                String img_url = knowledge.getImg_url();
                if (!isEmpty(img_url)) {
                    chat.setImg_url(img_url);
                } else {
                    chat.setImg_url(null);
                }
                chat.setActionOption(knowledge == null ? "action_0" : knowledge.getActionOption());
            } else {
                chat.setActionOption("action_0");
                chat.setImg_url(null);
            }
            //获取随机答案
            if (isEmpty(answer)) {
                chat.setAvailable("1");
                chat.setImg_url(null);
            } else {
                chat.setAvailable("0");
            }
            chat.setAnswer(answer);
            insertChatLog(map);
        } else {
            chat.setAnswer(KnowledgeUtil.ARREARAGE_ANSWER);
            chat.setImg_url(null);
        }
        if (!isEmpty(chat.getAnswer()) && chat.getAnswer().contains("http://ok7ouv8bt.bkt.clouddn.com")) {
            chat.setAnswer("");
        } else if (!isEmpty(chat.getAnswer()) && chat.getAnswer().contains("http://oky49sknd.bkt.clouddn.com")) {
            chat.setImg_url(chat.getAnswer());
            chat.setAnswer("限时45秒");
        }
        return chat;
    }

    @Override
    public Chat getWxChat(Chat chat) {
        System.out.println("程序开始执行...");
        String answer = null;
        Map<String, String> map = new HashMap<>();
        if (chat.getUuid() != null) {
            chat.setAvailable("1");
            map=this.getAnswerByOriginalProblem(chat);
            answer = map.get("answer");
            map.put("dataSource", "B");
            if (isEmpty(answer)){
                chat = getInvoke(chat);
                answer = chat.getAnswer();
            }
            if (!isEmpty(answer)) {
                map.put("username", chat.getUsername());
                map.put("question", chat.getQuestion());
                map.put("answer", answer);
                map.put("appKey", chat.getUnique_id());
                map.put("uuid", chat.getUuid());
                map.put("enabled", "0");
                map.put("extra4", "1");
            }
        }
        //查询企业知识库
        if (isEmpty(answer)) {
            chat.setAvailable("1");
            map = this.getAnswer(chat);
            answer = map.get("answer");
        }
        //查询通用知识库
        if ((isEmpty(answer)) && (isEmpty(chat.getEnable()) || chat.getEnable().equalsIgnoreCase("false"))) {
            chat.setAvailable("0");
            map = this.getAnswer(chat);
            if (!isEmpty(chat.getRobotName()) && !isEmpty(map.get("answer"))) {
                answer = map.get("answer").replace("小谛", chat.getRobotName());
            } else {
                answer = map.get("answer");
            }
            map.put("answer", answer);
        }
        if (chat.getFromusername() != null) {
            map.put("extra4", "2");
        } else {
            map.put("extra4", "1");
        }
        //知识库频次
        if (map.containsKey("id") && map.get("id") != null) {
            knowledgeMapper.updateFrequency(Integer.valueOf(map.get("id")));
        }
        //获取随机答案
        if (isEmpty(answer)) {
            answer = this.getRandomAnswers(chat);
            map.put("answer", answer);
        }
        chat.setAnswer(answer);
        insertChatLog(map);
        return chat;
    }

    //获取应用答案
    public Chat getInvoke(Chat chat) {
        String return_question=chat.getQuestion();
        String answer = null;
        String result = null;
        Chat chatCache = new Chat();
        //调用应用
        ExternalOptions options = new ExternalOptions();
        List<Chat> chatList = new ArrayList<>();
        if (!isEmpty(chat.getUuid())) {
            chatList = redisCache.get(chat.getUuid());
        }
        if (chatList == null) {
            chatList = new ArrayList<>();
        }
        String lastScene = null;
        String lastQuestion = null;
        if (chatList != null && chatList.size() > 0) {
            lastScene = chatList.get(chatList.size() - 1).getScene();
            lastQuestion = chatList.get(chatList.size() - 1).getQuestion();
        }
        String chat_question = chat.getQuestion();
        if (!isEmpty(chat_question)) {
            chat_question = chat_question.replace("+", "加").replace("-", "减").replace("*", "乘").replace("/", "除").replace("%", "取余").replace("×", "乘").replace("÷", "除");
        }
        String str_question = JSON.parseObject(semanticsService.inputHandling(chat_question, lastScene)).getString("qieci");
        if ((isEmpty(lastScene) || !lastScene.equals("音乐播放")) && inferenceEngineService.changjingjs(str_question, null).equals("音乐播放")) {
            lastScene = "音乐播放";
            str_question = JSON.parseObject(semanticsService.inputHandling(chat_question, lastScene)).getString("qieci");
        }
        String str_scene = inferenceEngineService.changjingjs(str_question, null);
        JSONObject jsonObject = JSON.parseObject(semanticsService.keywordProcessing(str_question));
        options = getOptions(jsonObject, chat, options);
        options.setLastQuestion(isEmpty(lastQuestion) ? " " : lastQuestion.trim().replace(" ", ""));
        Integer userId = Universe.current().getUserId();
//        options.setIntel_calendar(chat.getIntel_calendar());
        options.setThisScene(str_scene);
        options.setLastScene(lastScene);
        if (null != userId) {
            Robot robot = robotMapper.getByUserId(userId);
            if (null != robot) {
                options.setOwnUniqueId(robot.getUniqueId());
            }
        }
        if (!isEmpty(lastScene)) {
            options.setScene(lastScene.replace(" ", ""));

            result = externalApplicationService.invoke(options);
            chat = analysisJson(chat, result);
            answer = chat.getAnswer();
            if (!isEmpty(answer)) {
                chat.setAnswer(isSensitiveWord(answer));
                chat.setScene(lastScene);
                chatList.add(chat);
                redisCache.put(chat.getUuid(), chatList);
            }
        }
        if (isEmpty(answer) && !isEmpty(str_scene)) {
            options.setScene(str_scene.replace(" ", ""));
            result = externalApplicationService.invoke(options);
            chat = analysisJson(chat, result);
            answer = chat.getAnswer();
            if (!isEmpty(answer)) {
                chat.setAnswer(isSensitiveWord(answer));
                chat.setScene(str_scene);
                chatList.add(chat);
                if (chatList.size() > 3) {
                    List<Chat> chats = new ArrayList<>();
                    redisCache.del(chat.getUuid());
                    chats.add(chatList.get(chatList.size() - 3));
                    chats.add(chatList.get(chatList.size() - 2));
                    chats.add(chatList.get(chatList.size() - 1));
                    redisCache.put(chat.getUuid(), chats);
                } else {
                    redisCache.put(chat.getUuid(), chatList);
                }
            }
        }
        if (!isEmpty(chat.getAnswer()) && chat.getAnswer().contains("http://ok7ouv8bt.bkt.clouddn.com")) {
            chat.setImg_url(chat.getAnswer());
        }
        chat.setQuestion(return_question);
        return chat;
    }

    public String getRandomAnswers(Chat chat) {
        String answer = null;
        Random random = new Random();
        List<String> list = new ArrayList();
        if (!isEmpty(chat.getAnswer1()) && !chat.getAnswer1().equals("None")) {
            list.add(chat.getAnswer1());
        }
        if (!isEmpty(chat.getAnswer2()) && !chat.getAnswer1().equals("None")) {
            list.add(chat.getAnswer2());
        }
        if (!isEmpty(chat.getAnswer3()) && !chat.getAnswer1().equals("None")) {
            list.add(chat.getAnswer3());
        }
        if (!isEmpty(chat.getAnswer4()) && !chat.getAnswer1().equals("None")) {
            list.add(chat.getAnswer4());
        }
        if (!isEmpty(chat.getAnswer5()) && !chat.getAnswer1().equals("None")) {
            list.add(chat.getAnswer5());
        }
        if (list != null && list.size() == 1) {
            answer = list.get(0);
        } else if (list != null && list.size() > 1) {
            int result = random.nextInt(list.size() - 1);
            answer = list.get(result);
        }
        if (isEmpty(answer)) {
            answer = inferenceEngineService.suijidaan().split("\\|")[0];
        }
        return answer;
    }

    /**
     * @return C2 获取上句场景
     * N2 获取上句知识库缓存
     * R2 获取上句输入
     * G2 获取上句关键字
     * D2 获取上句答案
     * G3 获取上上句关键字
     * R3 获取上上句输入
     * username 企业账号
     * @throws
     */
    public Map<String, String> getAnswer(Chat chat) {
        String C2 = "";
        String N2 = "";
        String R2 = "";
        String G2 = "";
        String D2 = "";
        String G3 = "";
        String R3 = "";
        String G7 = "";
        String D7 = "";
        List<Chat> chatList = new ArrayList<>();
        if (chat.getUuid() != null) {
            chatList = redisCache.get(chat.getUuid());
        }
        if (chatList != null && chatList.size() > 0) {
            C2 = chatList.get(chatList.size() - 1).getScene();
            R2 = chatList.get(chatList.size() - 1).getQuestion();
            G2 = chatList.get(chatList.size() - 1).getKeywords();
            D2 = chatList.get(chatList.size() - 1).getAnswer();
        }
        if (chatList != null && chatList.size() > 1) {
            G3 = chatList.get(chatList.size() - 2).getKeywords();
            R3 = chatList.get(chatList.size() - 2).getQuestion();
        }

        Map<String, String> result = inferenceEngineService.tuiliji(chat, C2, N2, R2, G2, D2, G3, R3);
        String answer = result.get("answer").replace(" ", "");
        String isAvaliableAnswer = null;
        if (isEmpty(answer)) {
            if (!isCollectionEmpty(chat.getInvalids())) {
                int x = (int) (Math.random() * chat.getInvalids().size());
                isAvaliableAnswer = chat.getInvalids().get(x);
            } else {
                isAvaliableAnswer = inferenceEngineService.suijidaan().split("\\|")[0];
            }
            result.put("isAvaliableAnswer", isAvaliableAnswer);
            result.put("enabled", "1");
        } else {
            result.put("enabled", "0");
        }
        result.put("uuid", chat.getUuid());
        result.put("appKey", chat.getUnique_id());
        if (!isEmpty(chat.getUsername())) {
            result.put("username", chat.getUsername());
        } else if (!isEmpty(Universe.current().getUserName())) {
            result.put("username", Universe.current().getUserName());
        } else {
            result.put("username", "13991358085");
        }
        return result;
    }

    public Map<String, String> getAnswerByOriginalProblem(Chat chat) {
        String C2 = "";
        String N2 = "";
        String R2 = "";
        String G2 = "";
        String D2 = "";
        String G3 = "";
        String R3 = "";
        String G7 = "";
        String D7 = "";
        List<Chat> chatList = new ArrayList<>();
        if (chat.getUuid() != null) {
            chatList = redisCache.get(chat.getUuid());
        }
        if (chatList != null && chatList.size() > 0) {
            C2 = chatList.get(chatList.size() - 1).getScene();
            R2 = chatList.get(chatList.size() - 1).getQuestion();
            G2 = chatList.get(chatList.size() - 1).getKeywords();
            D2 = chatList.get(chatList.size() - 1).getAnswer();
        }
        if (chatList != null && chatList.size() > 1) {
            G3 = chatList.get(chatList.size() - 2).getKeywords();
            R3 = chatList.get(chatList.size() - 2).getQuestion();
        }

        Map<String, String> result = inferenceEngineService.tuilijiByOriginalProblem(chat, C2, N2, R2, G2, D2, G3, R3);
        String answer = result.get("answer").replace(" ", "");
        String isAvaliableAnswer = null;
        if (!isEmpty(answer)) {
            result.put("enabled", "0");
        }
        result.put("uuid", chat.getUuid());
        result.put("appKey", chat.getUnique_id());
        if (!isEmpty(chat.getUsername())) {
            result.put("username", chat.getUsername());
        } else if (!isEmpty(Universe.current().getUserName())) {
            result.put("username", Universe.current().getUserName());
        } else {
            result.put("username", "13991358085");
        }
        return result;
    }

    //记录聊天日志
    public void insertChatLog(Map<String, String> map) {
        String ip = Universe.current().getIp();
        ChatLog chatLog = new ChatLog();
        chatLog.setUsername(map.get("username"));
        chatLog.setIp(ip);
        if (map.get("username") == "13991358085") {
            chatLog.setOwner(1);
            chatLog.setOwnerType(2);
        }
        chatLog.setApp_key(map.get("appKey"));
        chatLog.setQuestion(map.get("question"));
        chatLog.setAnswer(map.get("answer"));
        chatLog.setUuid(map.get("uuid"));
        chatLog.setExtra1(map.get("enabled"));
        chatLog.setExtra4(map.get("extra4"));
        if (isEmpty(map.get("charge_mode"))) {
            chatLog.setCharge_mode("普通模式");
        } else {
            chatLog.setCharge_mode(map.get("charge_mode"));
        }
        chatLog.setDataSource(map.get("dataSource"));
        if (null != Universe.current().getUserName()) {
            chatLog.setLoginUsername(Universe.current().getUserName());
        }
        chatLogMongoService.create(chatLog);
        if (map.get("enabled") != null && map.get("enabled").equals("1")) {
            if (chatLog.getUuid() != null) {
                invalidQuestionService.insert(chatLog);
            }
        }
    }

    private String isSensitiveWord(String string) {
        SensitiveWordFilter sFilter = new SensitiveWordFilter();
        sFilter.initFilter();
        sFilter.findSensitiveWordInTxt(string);
        return sFilter.printResult(string);
    }

    private ExternalOptions getOptions(JSONObject jsonObject, Chat chat, ExternalOptions options) {
        options.setKw1(jsonObject.getString("gjz1").trim());
        options.setKw2(jsonObject.getString("gjz2").trim());
        options.setKw3(jsonObject.getString("gjz3").trim());
        options.setKw4(jsonObject.getString("gjz4").trim());
        options.setKw5(jsonObject.getString("gjz5").trim());
        options.setUuid(chat.getUuid().trim());
        options.setUserId(chat.getAccountId());
        options.setRobotName(chat.getRobotName());
        options.setSource(chat.getSource());
        String reg = "[!！?？。，,''“”；;:：]";
        Pattern pat = Pattern.compile(reg);
        Matcher mat = pat.matcher(chat.getQuestion());
        chat.setQuestion(mat.replaceAll(""));
        options.setQuestion(isEmpty(chat.getQuestion()) ? "" : chat.getQuestion().trim().replace(" ", ""));
        options.setUniqueId(chat.getUnique_id());
        return options;
    }

    private Chat analysisJson(Chat chat, String result) {
        List<ChangeBook> changeBookList = new ArrayList<>();
        if (!isEmpty(result)) {
            try {
                if (result.contains("object")&&result.contains("送书")) {
                    org.json.JSONObject jsonObject = str2json(result);
                    String str_object = (String) jsonObject.get("object");
                    JSONArray jsonArray = new JSONArray(str_object);
                    for (Object object : jsonArray) {
                        ChangeBook changeBook = new ChangeBook();
                        org.json.JSONObject jsonObject1 = (org.json.JSONObject) object;
                        changeBook.setAuthor(jsonObject1.getString("author"));
                        changeBook.setName(jsonObject1.getString("name"));
                        changeBook.setPicture(jsonObject1.getString("picture"));
                        changeBook.setPrice(jsonObject1.getString("price"));
                        changeBook.setShare(jsonObject1.getString("share"));
                        changeBook.setSynopsis(jsonObject1.getString("synopsis"));
                        changeBookList.add(changeBook);
                    }
                    chat.setChangeBooks(changeBookList);
                }else if (result.contains("mp3Url")&&str(str2json(result).get("domain")).equals("music")){
                    org.json.JSONObject jsonObject1=str2json(result);
                    org.json.JSONObject object=jsonObject1.getJSONObject("object");
                    String mp3Url=object.getString("mp3Url");
                    chat.setMp3Url(mp3Url);
                }
                chat.setAnswer(str(str2json(result).get("retData")));
            } catch (Exception e) {
                LOGGER.error("error occurred during get answer..." + e.getMessage());
            }
        }
        return chat;
    }

    private Chat getApplication(Chat chat) {
        String question = chat.getQuestion();
        List<Chat> chatList = new ArrayList<>();
        String lastScene=null;
        if (!isEmpty(chat.getUuid())) {
            chatList = redisCache.get(chat.getUuid());
        }
        if (chatList == null) {
            chatList = new ArrayList<>();
        }else {
            lastScene=chatList.get(chatList.size()-1).getScene();
        }
        if (!isEmpty(question)) {
            //判断句式 （告诉。。。，。。。）
            if (question.startsWith("告诉") && question.indexOf("，") != -1 && (question.indexOf("告诉") < question.indexOf("，"))) {
                if (!isEmpty(Universe.current().getUserName())) {
                    //根据当前用户id获取机器人
                    Robot robot = robotMapper.getByUserId(Universe.current().getUserId());
                    //截取字符串放入数据库表中
                    String robotname = question.substring(question.indexOf("告诉") + 2, question.indexOf("，"));
                    List<Robot> robots = robotMapper.getByName(robotname);
                    if (robot == null || robots.size() == 0) {
                        chat.setAnswer("没有找到此机器人名，通知失败！！！");
                        return chat;
                    }
                    String message = question.substring(question.indexOf("，") + 1);
                    TeleOther teleOther = new TeleOther();
                    teleOther.setForword_name(robot.getName());
                    teleOther.setReceive_name(robotname);
                    teleOther.setMessage(message);
                    teleOther.setCreatedBy(Universe.current().getUserName());
                    teleOther.setForword_unique_id(robot.getUniqueId());
                    teleOtherMapper.create(teleOther);
                    chat.setAnswer("已通知成功！！");
                } else {
                    chat.setAnswer("请前往登录页面进行登录再发私信！");
                }
                return chat;
            }
            //变身。。。。
            if (question.startsWith("变身")) {
                String name = question.replace("变身","");
                if (!isEmpty(name)) {
                    List<Robot> robot = robotMapper.getByName(name);
                    if (robot != null && robot.size() > 0) {
                        chat.setSkip_url("/robot-company/" + robot.get(0).getUniqueId());
                        chat.setRobotName(robot.get(0).getName());
                        chat.setWelcome(robot.get(0).getWelcomes());
                        chat.setProfile(robot.get(0).getProfile());
                        //获取机器人用户信息
                        if (robot.get(0).getAccountId() != null && robot.get(0).getAccountId().intValue() != 0) {
                            Account account = accountMapper.get(robot.get(0).getAccountId());
                            chat.setUsername(account.getUserName());
                            chat.setHeadImgUrl(account.getHeadImgUrl());
                        }
                        //获取公司信息
                        if (robot.get(0).getCompanyId() != null && robot.get(0).getCompanyId().intValue() != 0) {
                            Company company = companyMapper.get(robot.get(0).getCompanyId());
                            chat.setCompanyName(company.getName());
                        }
                        chat.setAnswer("操作成功！");
                    }
                }
            }

            //查找。。。。
            if (question.startsWith("查找")) {
                String name = question.replace("查找","");
                if (!isEmpty(name)) {
//                    List<Company> companies = companyService.searchForKeyWord(name);
                    List<Robot> companies =robotMapper.getByRobotName(name);
                    if (companies == null || companies.size() == 0) {
                        chat.setAnswer("没有找到此机器人名");
                        return chat;
                    }

                    StringBuffer sb = new StringBuffer();
                    sb.append("<br/>");
                    if (companies != null && companies.size() > 0) {
                        for(int i = 0;i<companies.size();i++){
                            String robotName = companies.get(i).getName();
                            sb.append((i+1)+"."+robotName+"<br/>");
                        }
                        chat.setAnswer(sb.toString());
                    }
                }
            }


            //打电话：。。。。
            if (question.startsWith("打电话给")) {
                String name = question.replaceAll("打电话给","");
                if (!isEmpty(name)) {
                    try {
                        //根据机器人名称查找电话号码
                        List<Robot> robot =robotMapper.getTelByRobotName(name);
                        //根据公司名称查找电话号码
                        List<Robot> company =robotMapper.getTelByCompanyName(name);

                        if(robot.size()>0){
                            String tel =  robot.get(0).getName();
                            chat.setAnswer(tel);
                        }
                        else if(company.size()>0){
                            String tel =  company.get(0).getName();
                            chat.setAnswer(tel);
                        }
                    }catch (Exception e){
                        chat.setAnswer("没有找到联系人");
                    }
                }
            }



            //读取站内信
            if (question.indexOf("读取站内信") != -1||(!isEmpty(lastScene)&&lastScene.equals("读取站内信")&&question.contains("下一条"))) {
                if (!isEmpty(Universe.current().getUserName())) {
                    //根据当前用户id获取机器人
                    Robot robot = robotMapper.getByUserId(Universe.current().getUserId());
                    TeleOther teleOther=new TeleOther();
                    teleOther.setReceive_name(robot.getName());
                    List<TeleOther> teleOthers=teleOtherMapper.getCount(teleOther);
                    StringBuffer sb = new StringBuffer();
                    if(teleOthers!=null&&teleOthers.size()!=0){
                        chat.setAnswer(teleOthers.get(0).getForword_name()+"说："+teleOthers.get(0).getMessage());
                        teleOtherMapper.update(teleOthers.get(0));
                    }
                    /*if(teleOthers!=null&&teleOthers.size()!=0){
                        for (int i=0;i<teleOthers.size();i++){
                            sb.append((i+1)+"."+teleOthers.get(i).getForword_name()+"说："+teleOthers.get(i).getMessage()+"\n");
                            teleOtherMapper.update(teleOthers.get(i));
                        }
                    }else{
                        chat.setAnswer("暂时没有尚未读取的站内信！");
                    }
                    if("web".equals(chat.getSource())){
                        chat.setAnswer(sb.toString().replaceAll("\n","<br/>"));
                    }else{
                        chat.setAnswer(sb.toString());
                    }*/

                } else {
                    chat.setAnswer("请前往登录页面进行登录再读取站内信！");
                }
                chat.setScene("读取站内信");
                chatList.add(chat);
                redisCache.put(chat.getUuid(), chatList);
            }

        }
        return chat;
    }
}
