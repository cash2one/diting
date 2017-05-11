package com.diting.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.diting.cache.Cache;
import com.diting.core.Universe;
import com.diting.dao.AccountMapper;
import com.diting.dao.BaseInfoMapper;
import com.diting.dao.KnowledgeMapper;
import com.diting.elasticsearch.ElasticSearch;
import com.diting.model.Account;
import com.diting.model.BaseInfo;
import com.diting.model.CustomerSynonym;
import com.diting.model.Knowledge;
import com.diting.model.options.KnowledgeOptions;
import com.diting.model.result.Results;
import com.diting.model.views.AccountViews;
import com.diting.model.views.KnowledgeViews;
import com.diting.sensitivewords.SensitiveWordFilter;
import com.diting.service.InferenceEngineService;
import com.diting.service.KnowledgeService;
import com.diting.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.diting.util.Constants.DATE_FORMAT;
import static com.diting.util.Utils.isEmpty;

/**
 * Created by liufei on 2016/8/10.
 */
@SuppressWarnings("ALL")
@Service("knowledgeService")
//@Transactional
public class KnowledgeServiceImpl extends HessianClient implements KnowledgeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KnowledgeServiceImpl.class);


    @Autowired
    private KnowledgeMapper knowledgeMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InferenceEngineService inferenceEngineService;

    @Autowired
    private BaseInfoMapper baseInfoMapper;

    @Value("${remote.input.url}")
    private String input_url;

    @Value("${remote.keywords.url}")
    public String keyword_url;

    @Autowired
    @Qualifier("redisCache")
    private Cache redisCache;

    @Autowired
    private ElasticSearch elasticSearchService;

    @Override
    public Knowledge create(Knowledge knowledge) {
        knowledgeMapper.create(knowledge);
        return knowledge;
    }

    @Override
    public Knowledge getScene(Knowledge knowledge) {
        knowledge.setScene(getScene(knowledge.getQuestion()));
        return knowledge;
    }

    @Override
    public Map<String, String> complexCreate(Knowledge knowledge) {
        knowledge = setKnowledge(knowledge);
        Map<String, String> map = new HashMap<>();

        String isSensitiveWord = isSensitiveWord(knowledge.getAnswer());
        String isSensitiveWordQuestion=isSensitiveWord(knowledge.getQuestion());
//        List<Knowledge> knowledgeList = knowledgeMapper.getKnowledge(knowledge);
        List<Knowledge> knowledgeList=elasticSearchService.searchByQuestionAndAnswer(knowledge);
        if ((knowledgeList == null || knowledgeList.size() == 0) && isEmpty(isSensitiveWord)&&isEmpty(isSensitiveWordQuestion)) {
            knowledgeMapper.create(knowledge);
            elasticSearchService.create(knowledge.getId());
            map.put("result", KnowledgeUtil.CREATE_KNOWLEDGE_RESULT);
        } else if (knowledgeList != null && knowledgeList.size() > 0) {
            map.put("result", KnowledgeUtil.CREATE_KNOWLEDGE_FAILED);
        } else if (!isEmpty(isSensitiveWord)||!isEmpty(isSensitiveWordQuestion)) {
            map.put("result", KnowledgeUtil.ISSENSITIVEWORD + isSensitiveWord+isSensitiveWordQuestion);
        } else {
            map.put("result", KnowledgeUtil.CREATE_KNOWLEDGE_EXIST);
        }
        return map;
    }

    @Override
    public Map<String, String> complexTempCreate(Knowledge knowledge) {
        Map<String, String> map = new HashMap<>();
        String[] questions=knowledge.getQuestion().split("[.。?？!！]");
        String scene=null;
        List<Knowledge> knowledges=new ArrayList<>();
        String failed = null;
        String str_failedNum = "";
        int num=0;
        int i=0;
        for (String q:questions){
            Knowledge knowledge1=new Knowledge();
            knowledge1.setQuestion(q);
            knowledge1.setAnswer(q);
            knowledge1.setAccountId(15);
            knowledge1=setKnowledge(knowledge1);
            knowledges.add(knowledge1);
        }
//        knowledge = setKnowledge(knowledge);
        StringBuffer strings=new StringBuffer();
        for (Knowledge knowledge1:knowledges){
            if (!isEmpty(knowledge1.getScene())){
                strings.append(knowledge1.getScene()+",");
            }
        }
        String[] strings1=null;
        if (!isEmpty(strings.toString())){
            strings1=strings.substring(0,strings.length()-1).split(",");
            scene= GetMaxStrUtil.getMaxString(strings1);
        }
        for (Knowledge knowledge1:knowledges){
            i++;
            knowledge1.setScene(scene);
            List<Knowledge> knowledgeList=elasticSearchService.searchByQuestionAndAnswer(knowledge1);
            String isSensitiveWord = isSensitiveWord(knowledge1.getAnswer());
            String isSensitiveWordQuestion=isSensitiveWord(knowledge1.getQuestion());
            if ((knowledgeList == null || knowledgeList.size() == 0) && knowledge1.getQuestion().length() <= 60 && knowledge1.getAnswer().length() <= 240 && (isEmpty(knowledge1.getScene()) || !isEmpty(knowledge1.getScene()) && knowledge1.getScene().length() <= 20) && isEmpty(isSensitiveWord)&&isEmpty(isSensitiveWordQuestion)) {
                knowledgeMapper.create(knowledge1);
                elasticSearchService.create(knowledge1.getId());
            } else {
                ++num;
                failed = num + ",";
                map.put("failed", failed.substring(failed.length() - 1));
                str_failedNum += i + ",";
            }
        }
        if (num > 0) {
            map.put("result", "数据重复导入或问题、答案或场景长度超出限制");
            map.put("num", "失败条数:" + num + " 第 " + str_failedNum.substring(0, str_failedNum.length() - 1) + "条上传失败");
        }
        if (num == 0) {
            map.put("result", "导入成功");
        }
        return map;
    }

    @Override
    public Map<String, String> baseKnowledgeCreate(Knowledge knowledge) {
        knowledge = setKnowledge(knowledge);
        knowledge.setAccountId(-1);
        knowledge.setCompanyId(-1);
        Map<String, String> map = new HashMap<>();
//        List<Knowledge> knowledgeList = knowledgeMapper.getKnowledge(knowledge);
        List<Knowledge> knowledgeList=elasticSearchService.searchByQuestionAndAnswer(knowledge);
        String isSensitiveWord = isSensitiveWord(knowledge.getAnswer());
        String isSensitiveWordQuestion=isSensitiveWord(knowledge.getQuestion());
        if ((knowledgeList == null || knowledgeList.size() == 0) && isEmpty(isSensitiveWord)&&isEmpty(isSensitiveWordQuestion)) {
            knowledgeMapper.create(knowledge);
            elasticSearchService.create(knowledge.getId());
            map.put("result", KnowledgeUtil.CREATE_KNOWLEDGE_RESULT);
        } else if (knowledgeList != null && knowledgeList.size() > 0) {
            map.put("result", KnowledgeUtil.CREATE_KNOWLEDGE_FAILED);
        } else if (!isEmpty(isSensitiveWord)||!isEmpty(isSensitiveWordQuestion)) {
            map.put("result", KnowledgeUtil.ISSENSITIVEWORD + isSensitiveWord+isSensitiveWordQuestion);
        } else {
            map.put("result", KnowledgeUtil.CREATE_KNOWLEDGE_EXIST);
        }
        return map;
    }

    @Override
    public Knowledge update(Knowledge knowledge) {
        knowledge = setKnowledge(knowledge);
        String isSensitiveWord = isSensitiveWord(knowledge.getAnswer());
        String isSensitiveWordQuestion=isSensitiveWord(knowledge.getQuestion());
        if (isEmpty(isSensitiveWord)||isEmpty(isSensitiveWordQuestion)){
            knowledgeMapper.update(knowledge);
            LOGGER.info("create ES index by knowledgeId: " + knowledge.getId());
            elasticSearchService.create(knowledge.getId());
        }

        return knowledge;
    }

    @Override
    public Knowledge baseKnowledgeUpdate(Knowledge knowledge) {
        knowledge = setKnowledge(knowledge);
        knowledge.setAccountId(-1);
        knowledge.setCompanyId(-1);
        String isSensitiveWord=isSensitiveWord(knowledge.getAnswer());
        String isSensitiveWordQuestion=isSensitiveWord(knowledge.getQuestion());
        if (isEmpty(isSensitiveWord)||isEmpty(isSensitiveWordQuestion)){
            knowledgeMapper.update(knowledge);
            elasticSearchService.create(knowledge.getId());
        }
        return knowledge;
    }

    @Override
    public Boolean delete(Integer knowledgeId) {
        elasticSearchService.delete(knowledgeId);
        return knowledgeMapper.delete(knowledgeId);
    }

    @Override
    public Knowledge get(Integer knowledgeId) {
        return knowledgeMapper.get(knowledgeId);
    }


    @Override
    public Results<Knowledge> searchForPage(KnowledgeOptions options) {
        Results results = new Results();
        List<Knowledge> accounts = knowledgeMapper.searchForPage(options);
        results.setItems(accounts);
        results.setTotal(options.getTotalRecord());
        return results;
    }

    @Override
    public Boolean batchDelete(String[] strings) {
        List<Knowledge> list = new ArrayList<>();
        for (String string : strings) {
            Knowledge knowledge = knowledgeMapper.get(Integer.valueOf(string));
            list.add(knowledge);
            elasticSearchService.delete(knowledge.getId());
        }
        return knowledgeMapper.batchUpdate(list);
    }

    @Override
    public Boolean adminBatchDelete(String[] strings) {
        List<Knowledge> list = new ArrayList<>();
        for (String string : strings) {
            Knowledge knowledge = new Knowledge();
            knowledge.setId(Integer.valueOf(string));
            list.add(knowledge);
            elasticSearchService.delete(Integer.valueOf(string));
        }
        return knowledgeMapper.batchUpdate(list);
    }

    @Override
    public List<Knowledge> findKnowledgesByAccountId(Integer accountId) {
        return knowledgeMapper.findKnowledgesByAccountId(accountId);
    }

    @Override
    public Map batchInsert(List<Map> maps) {
        Map<String, String> map = new HashMap<>();
        int num = 0;
        List<Knowledge> list = new ArrayList<>();
        String failed = null;
        String str_failedNum = "";
        for (int i = 0; i < maps.size(); i++) {
            Date date=Utils.now();
            Knowledge knowledge = new Knowledge();
            if (maps.get(i).get("question") == null || maps.get(i).get("answer") == null) {
                break;
            }
            knowledge.setQuestion(maps.get(i).get("question").toString());
            knowledge.setAnswer(maps.get(i).get("answer").toString());
            if (maps.get(i).get("scene") != null) {
                knowledge.setScene(maps.get(i).get("scene").toString());
            }
            if (null != maps.get(i).get("action")) {
                knowledge.setActionOption(maps.get(i).get("action").toString());
            } else {
                knowledge.setActionOption("action_0");
            }
            knowledge = setKnowledge(knowledge);
//            List<Knowledge> knowledgeList = knowledgeMapper.getKnowledge(knowledge);
            List<Knowledge> knowledgeList=elasticSearchService.searchByQuestionAndAnswer(knowledge);
            String isSensitiveWord = isSensitiveWord(knowledge.getAnswer());
            String isSensitiveWordQuestion=isSensitiveWord(knowledge.getQuestion());
            if ((knowledgeList == null || knowledgeList.size() == 0) && knowledge.getQuestion().length() <= 60 && knowledge.getAnswer().length() <= 240 && (isEmpty(knowledge.getScene()) || !isEmpty(knowledge.getScene()) && knowledge.getScene().length() <= 20) && isEmpty(isSensitiveWord)&&isEmpty(isSensitiveWordQuestion)) {
                knowledgeMapper.create(knowledge);
                elasticSearchService.create(knowledge.getId());
                Date date1=Utils.now();
                System.out.println("每新增一条知识的时间: "+(date1.getTime()-date.getTime()));
            } else {
                ++num;
                failed = num + ",";
                map.put("failed", failed.substring(failed.length() - 1));
                str_failedNum += i + 1 + ",";
            }
        }
        if (num > 0) {
            map.put("result", "数据重复导入或问题、答案或场景长度超出限制");
            map.put("num", "失败条数:" + num + " 第 " + str_failedNum.substring(0, str_failedNum.length() - 1) + "条上传失败");
        }
        if (num == 0) {
            map.put("result", "导入成功");
        }
        return map;
    }

    @Override
    public Map baseBatchInsert(List<Map> maps) {
        Map<String, String> map = new HashMap<>();
        int num = 0;
        List<Knowledge> list = new ArrayList<>();
        String str_failedNum = "";
        int i = 0;
        for (Map map1 : maps) {
            i++;
            Knowledge knowledge = new Knowledge();
            if (map1.get("question") == null || map1.get("answer") == null) {
                break;
            }
            knowledge.setQuestion(map1.get("question").toString());
            knowledge.setAnswer(map1.get("answer").toString());
            if (map1.get("scene") != null) {
                knowledge.setScene(map1.get("scene").toString());
            }
            if (null != map1.get("action")) {
                knowledge.setActionOption(map1.get("action").toString());
            } else {
                knowledge.setActionOption("action_0");
            }
            knowledge = setKnowledge(knowledge);
            knowledge.setCompanyId(-1);
            knowledge.setAccountId(-1);
//            List<Knowledge> knowledgeList = knowledgeMapper.getKnowledge(knowledge);
            List<Knowledge> knowledgeList=elasticSearchService.searchByQuestionAndAnswer(knowledge);
            String isSensitiveWord = isSensitiveWord(knowledge.getAnswer());
            String isSensitiveWordQuestion=isSensitiveWord(knowledge.getQuestion());
            if ((knowledgeList == null || knowledgeList.size() == 0) && knowledge.getQuestion().length() <= 60 && knowledge.getAnswer().length() <= 240 && isEmpty(isSensitiveWord)&&isEmpty(isSensitiveWordQuestion)) {
                knowledgeMapper.create(knowledge);
                elasticSearchService.create(knowledge.getId());
            } else {
                num++;
                str_failedNum += i + 1 + ",";
            }
        }
        if (num > 0) {
            map.put("result", "数据重复导入或问题与答案长度超出限制");
            map.put("num", "失败条数:" + num + " 第 " + str_failedNum.substring(0, str_failedNum.length() - 1) + "条上传失败");
        }
        return map;
    }

    @Override
    public List<Knowledge> findCompanyKnowledgeList(KnowledgeOptions options) {
        Integer accountId = Universe.current().getUserId();
        Integer companyId = accountMapper.get(accountId).getCompanyId();
        options.setCompanyId(companyId);
        return knowledgeMapper.companyKnowledgeList(options);
    }

    @Override
    public List<Knowledge> findBaseKnowledgeList(KnowledgeOptions options) {
        options.setCompanyId(-1);
        return knowledgeMapper.companyKnowledgeList(options);
    }

    @Override
    public Results<Knowledge> searchCompanyKnowledgeForPage(KnowledgeOptions options) {
        Results results = new Results();
        List<Knowledge> accounts = knowledgeMapper.searchCompanyKnowledgeForPage(options);
        results.setItems(accounts);
        results.setTotal(options.getTotalRecord());
        return results;
    }

    @Override
    public void updateAllKeys() {
        List<Knowledge> knowledges = knowledgeMapper.getKnowledgeList();
        System.out.println("知识库总数：" + knowledges.size());
//        for (Knowledge knowledge:knowledges){
//            elasticSearchService.delete(knowledge.getId());
//        }

        Map<String, String> map = new HashMap<>();
        List<Knowledge> knowledgeList = new ArrayList<>();
        int i = 0;
        int j = 0;
        for (Knowledge knowledge : knowledges) {
            i++;
            map.put("question", !isEmpty(knowledge.getQuestion())?knowledge.getQuestion().trim():"");
            map.put("scene", !isEmpty(knowledge.getScene())?knowledge.getScene().trim():"1");
            String result = SemanticsUtil.getRequest1(input_url, map);
            if (isEmpty(result) || !result.contains("qieci")) {
                System.out.println(knowledge.getId() + "result: " + result);
                continue;
            }
            String str_question = JSON.parseObject(result).getString("qieci");
            map.remove("question");
            map.remove("scene");
            map.put("keyword", str_question);
            String keywords = SemanticsUtil.getRequest1(keyword_url, map);
            JSONObject jsonObject = JSON.parseObject(keywords);
            if (isEmpty(keywords) || !keywords.contains("gjz1")) {
                System.out.println(knowledge.getId() + "keywords: " + keywords);
                continue;
            }
            String new_kw1 = jsonObject.getString("gjz1");
            String new_kw2 = jsonObject.getString("gjz2");
            String new_kw3 = jsonObject.getString("gjz3");
            String new_kw4 = jsonObject.getString("gjz4");
            String new_kw5 = jsonObject.getString("gjz5");
            if (!knowledge.getKw1().equals(new_kw1) || !knowledge.getKw2().equals(new_kw2) || !knowledge.getKw3().equals(new_kw3) || !knowledge.getKw4().equals(new_kw4) || !knowledge.getKw5().equals(new_kw5)) {
                j++;
                System.out.println("i=" + i + "     j=" + j + "   j/i=" + ((double) j / i));
                knowledge.setKw1(new_kw1);
                knowledge.setKw2(new_kw2);
                knowledge.setKw3(new_kw3);
                knowledge.setKw4(new_kw4);
                knowledge.setKw5(new_kw5);
                knowledgeList.add(knowledge);
//                knowledgeMapper.update(knowledge);
                knowledgeMapper.batchUpdateKeywords(knowledgeList);
                elasticSearchService.create(knowledge.getId());
                knowledgeList.clear();
            } else {
//                System.out.println("else:" + i);
//                knowledgeList.add(knowledge);
//                elasticSearchService.create(knowledge.getId());
//                knowledgeList.clear();
            }
//            System.out.println("else: "+ i);
        }
    }

    @Override
    public void createBaseKnowledgeInfo(BaseInfo baseInfo) {
        Integer accountId = Universe.current().getUserId();
        Account account = accountMapper.get(accountId);
        baseInfo.setAccountId(accountId);
        baseInfo.setCompanyId(account.getCompanyId());
        List<BaseInfo> baseInfoList=baseInfoMapper.get(account.getCompanyId());
        BaseInfo baseInfo1 = baseInfoList==null||baseInfoList.size()==0?null:baseInfoList.get(0);
        if (baseInfo1 == null) {
            baseInfoMapper.create(baseInfo);
        } else {
            baseInfo.setId(baseInfo1.getId());
            baseInfoMapper.update(baseInfo);
        }
        if (!isEmpty(baseInfo.getCompanyName())) {
            String[] str_companyName = ProblemUtil.COMPANY_NAMES.split(",");
            createBaseKnowledge(str_companyName, baseInfo.getCompanyName());
        }
        if (!isEmpty(baseInfo.getCompanyUrl())) {
            String[] str_companyUrl = ProblemUtil.COMPANY_URLS.split(",");
            createBaseKnowledge(str_companyUrl, baseInfo.getCompanyUrl());
        }
        if (!isEmpty(baseInfo.getCompanyAddress())) {
            String[] str_companyAddress = ProblemUtil.COMPANY_ADDRESS.split(",");
            createBaseKnowledge(str_companyAddress, baseInfo.getCompanyAddress());
        }
        if (!isEmpty(baseInfo.getBusLine())) {
            String[] str_busLine = ProblemUtil.BUS_LINE.split(",");
            createBaseKnowledge(str_busLine, baseInfo.getBusLine());
        }
        if (!isEmpty(baseInfo.getCompanyTel())) {
            String[] str_companyTel = ProblemUtil.COMPANY_TELS.split(",");
            createBaseKnowledge(str_companyTel, baseInfo.getCompanyTel());
        }
        if (!isEmpty(baseInfo.getCompanyBusiness())) {
            String[] str_companyBusiness = ProblemUtil.COMPANY_BUSINESSES.split(",");
            createBaseKnowledge(str_companyBusiness, baseInfo.getCompanyBusiness());
        }
        if (!isEmpty(baseInfo.getCompanyWeChat())) {
            String[] str_companyWeChat = ProblemUtil.COMPANY_WECHAT.split(",");
            createBaseKnowledge(str_companyWeChat, baseInfo.getCompanyWeChat());
        }
        if (!isEmpty(baseInfo.getCompanySize())) {
            String[] str_companySize = ProblemUtil.COMPANY_SIZE.split(",");
            createBaseKnowledge(str_companySize, baseInfo.getCompanySize());
        }
        if (!isEmpty(baseInfo.getCompanyProfile())) {
            String[] str_companyProfile = ProblemUtil.COMPANY_PROFILE.split(",");
            createBaseKnowledge(str_companyProfile, baseInfo.getCompanyProfile());
        }
        if (!isEmpty(baseInfo.getProductIntroduction())) {
            String[] str_productIntroduction = ProblemUtil.PRODUCT_INTRPDUCTION.split(",");
            createBaseKnowledge(str_productIntroduction, baseInfo.getProductIntroduction());
        }
        if (!isEmpty(baseInfo.getTechnologyAdvantage())) {
            String[] str_technologyAdvantage = ProblemUtil.TECHNOLOGY_ADVANTAGE.split(",");
            createBaseKnowledge(str_technologyAdvantage, baseInfo.getTechnologyAdvantage());
        }
        if (!isEmpty(baseInfo.getIndustryWhere())) {
            String[] str_industryWhere = ProblemUtil.INDUSTRYWHERE.split(",");
            createBaseKnowledge(str_industryWhere, baseInfo.getIndustryWhere());
        }
        if (!isEmpty(baseInfo.getDevelopmentProspect())) {
            String[] str_developmentProspect = ProblemUtil.DEVELOPMENT_PROSPECT.split(",");
            createBaseKnowledge(str_developmentProspect, baseInfo.getDevelopmentProspect());
        }
        if (!isEmpty(baseInfo.getCorporateCulture())) {
            String[] str_corporateCulture = ProblemUtil.CORPORATECULTRUE.split(",");
            createBaseKnowledge(str_corporateCulture, baseInfo.getCorporateCulture());
        }
        if (!isEmpty(baseInfo.getWorkShift())) {
            String[] str_workShift = ProblemUtil.WORKSHIFT.split(",");
            createBaseKnowledge(str_workShift, baseInfo.getWorkShift());
        }
        if (!isEmpty(baseInfo.getCompanyLeadership())) {
            String[] companyLeadership = ProblemUtil.COMPANY_LEADERSHIP.split(",");
            createBaseKnowledge(companyLeadership, baseInfo.getCompanyLeadership());
        }
        if (!isEmpty(baseInfo.getFinancingSituation())) {
            String[] financingSituation = ProblemUtil.FINANCING_SITUATION.split(",");
            createBaseKnowledge(financingSituation, baseInfo.getFinancingSituation());
        }
        if (!isEmpty(baseInfo.getHotPositions())) {
            String[] hotPositions = ProblemUtil.HOT_POSITIONS.split(",");
            createBaseKnowledge(hotPositions, baseInfo.getHotPositions());
        }
    }

    @Override
    public BaseInfo getBaseInfoByCompanyId() {
        Integer accountId = Universe.current().getUserId();
        Account account = accountMapper.get(accountId);
        List<BaseInfo> baseInfos=baseInfoMapper.get(account.getCompanyId());
        return baseInfos==null||baseInfos.size()==0?null:baseInfos.get(0);
    }

    @Override
    public List<KnowledgeViews> searchKnowledgeNum(String type) {
        List<Knowledge> knowledgeList=new ArrayList<>();
        Date startTime=null;
        if (!isEmpty(type)){
            if (type.equals("today")){
                startTime=Utils.today();
                knowledgeList=knowledgeMapper.searchKnowledgeNum(startTime,null);
                return knowledgeNumByYesterdayOrToday(knowledgeList, startTime,2);
            }else if (type.equals("yesterday")){
                startTime=Utils.daysBefore(Utils.today(), 1);
                Date endTime=Utils.today();
                knowledgeList=knowledgeMapper.searchKnowledgeNum(startTime,endTime);
                return knowledgeNumByYesterdayOrToday(knowledgeList, startTime,2);
            }else if (type.equals("week")){
                startTime=Utils.daysBefore(Utils.today(), 7);
                Date endTime=Utils.today();
                knowledgeList=knowledgeMapper.searchKnowledgeNum(startTime,endTime);
                return knowledgeNumByWeek(knowledgeList,startTime,2);
            }else if (type.equals("month")){
                startTime=Utils.daysBefore(Utils.today(), 30);
                Date endTime=Utils.today();
                knowledgeList=knowledgeMapper.searchKnowledgeNum(startTime,endTime);
                return knowledgeNumByMonth(knowledgeList,startTime,2);
            }
            return null;
        }
        return null;
    }

    public Knowledge setKnowledge(Knowledge knowledge) {
        Integer userId = Universe.current().getUserId();
        if (null != knowledge.getAccountId()) {
            userId = knowledge.getAccountId();
        }
        Account account = accountMapper.get(userId);

        String question = knowledge.getQuestion();
        String answer = knowledge.getAnswer();
        if (question.contains("\n")) {
            knowledge.setQuestion(question.replace("\n", ""));
        }
        if (answer.contains("\n")) {
            knowledge.setAnswer(answer.replace("\n", ""));
        }
        String key = "CustomerSynonym" + account.getUserName();
        List<CustomerSynonym> customerSynonymList = redisCache.get(key);
        String synonymQuestion = knowledge.getQuestion();
        if (null != customerSynonymList && customerSynonymList.size() > 0) {
            for (int j = 0; j < customerSynonymList.size(); j++) {
                String[] strings=customerSynonymList.get(j).getWord_old().split("[;；]");
                for (String string:strings){
                    if (synonymQuestion.contains(string)) {
                        synonymQuestion = synonymQuestion.replace(string, customerSynonymList.get(j).getWord_new());
                    }
                }
            }
        }
        knowledge.setSynonymQuestion(synonymQuestion);
        String T = JSON.parseObject(semanticsService.inputHandling(knowledge.getSynonymQuestion(), knowledge.getScene())).getString("qieci");
        JSONObject jsonObject = JSON.parseObject(semanticsService.keywordProcessing(T));
        knowledge.setKw1(jsonObject.getString("gjz1"));
        knowledge.setKw2(jsonObject.getString("gjz2"));
        knowledge.setKw3(jsonObject.getString("gjz3"));
        knowledge.setKw4(jsonObject.getString("gjz4"));
        knowledge.setKw5(jsonObject.getString("gjz5"));
        knowledge.setHandleQuestion(T.replace(" ", ""));
        knowledge.setAccountId(userId);
        if (isEmpty(knowledge.getActionOption())) {
            knowledge.setActionOption("action_0");
        }
        Integer company_id = account.getCompanyId();
        knowledge.setCompanyId(company_id);
        if (knowledge.getEmotion() == 0) {
            knowledge.setEmotion(5);
        }
        if (isEmpty(knowledge.getScene())) {
            knowledge.setScene(getScene(knowledge.getQuestion()));
        }

        return knowledge;
    }

    public Integer searchKnowledgeCount(KnowledgeOptions options) {
        return knowledgeMapper.searchKnowledgeCount(options);
    }

    @Override
    public List<Knowledge> getKnowledgeByFrequencr(Knowledge knowledge) {
        return knowledgeMapper.getKnowledgeByFrequencr(knowledge);
    }

    @Override
    public int searchKnowledgeCountByAccountId(KnowledgeOptions options) {
        return knowledgeMapper.searchKnowledgeCountByAccountId(options);
    }

    public void createBaseKnowledge(String question[], String answer) {
        Knowledge knowledge = new Knowledge();
        knowledge.setAnswer(answer);
        for (String aQuestion : question) {
            knowledge.setQuestion(aQuestion);
            knowledge = setKnowledge(knowledge);
            List<Knowledge> knowledgeList = knowledgeMapper.getKnowledge(knowledge);
            if (knowledgeList == null || knowledgeList.size() == 0) {
                knowledgeMapper.create(knowledge);
                elasticSearchService.create(knowledge.getId());
            }
        }
    }

    private String isSensitiveWord(String string) {
        SensitiveWordFilter sFilter = new SensitiveWordFilter();
        sFilter.initFilter();
        sFilter.findSensitiveWordInTxt(string);
        return sFilter.printResult();
    }

    public List<KnowledgeViews> knowledgeNumByYesterdayOrToday(List<Knowledge> knowledgeList, Date begin, int t){
        int one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve,source1,source2,source3,source4,source5;
        one=two=three=four=five=six=seven=eight=nine=ten=eleven=twelve=0;
        String time=null;
        List<KnowledgeViews> accountViewsList=new ArrayList<>();
        for (Knowledge knowledge:knowledgeList){
            Date date=knowledge.getCreatedTime();
            int n= (int) ((date.getTime()-begin.getTime())/(2*60*60*1000));
            if (n==0){
                one+=1;
            }else if (n==1){
                two+=1;
            }else if (n==2){
                three+=1;
            }else if (n==3){
                four+=1;
            }else if (n==4){
                five+=1;
            }else if (n==5){
                six+=1;
            }else if (n==6){
                seven+=1;
            }else if (n==7){
                eight+=1;
            }else if (n==8){
                nine+=1;
            }else if (n==9){
                ten+=1;
            }else if (n==10){
                eleven+=1;
            }else if (n==11){
                twelve+=1;
            }
        }
        String strDate=DATE_FORMAT.format(begin);
        for (int i=0;i<12;i++){
            KnowledgeViews  knowledgeViews=new KnowledgeViews();
            if (i==0){
                time=strDate+" 0"+((i+1)*2)+":00:00";
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(one);
            }else if(i==1){
                time=strDate+" 0"+((i+1)*2)+":00:00";
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(two);
            }else if(i==2){
                time=strDate+" 0"+((i+1)*2)+":00:00";
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(three);
            }else if(i==3){
                time=strDate+" 0"+((i+1)*2)+":00:00";
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(four);
            }else if(i==4){
                time=strDate+" 0"+((i+1)*2)+":00:00";
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(five);
            }else if(i==5){
                time=strDate+" "+((i+1)*2)+":00:00";
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(six);
            }else if(i==6){
                time=strDate+" "+((i+1)*2)+":00:00";
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(seven);
            }else if(i==7){
                time=strDate+" "+((i+1)*2)+":00:00";
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(eight);
            }else if(i==8){
                time=strDate+" "+((i+1)*2)+":00:00";
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(nine);
            }else if(i==9){
                time=strDate+" "+((i+1)*2)+":00:00";
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(ten);
            }else if(i==10){
                time=strDate+" "+((i+1)*2)+":00:00";
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(eleven);
            }else if(i==11){
                time=strDate+" "+((i+1)*2)+":00:00";
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(twelve);
            }
            accountViewsList.add(knowledgeViews);
        }
        return accountViewsList;
    }
    public List<KnowledgeViews> knowledgeNumByWeek(List<Knowledge> knowledgeList,Date begin,int t){
        int one,two,three,four,five,six,seven;
        one=two=three=four=five=six=seven=0;
        String time=null;
        List<KnowledgeViews> knowledgeViewses=new ArrayList<>();
        for (Knowledge knowledge:knowledgeList){
            Date date=knowledge.getCreatedTime();
            int n= (int) ((date.getTime()-begin.getTime())/(24*60*60*1000));
            if (n==0){
                one+=1;
            }else if (n==1){
                two+=1;
            }else if (n==2){
                three+=1;
            }else if (n==3){
                four+=1;
            }else if (n==4){
                five+=1;
            }else if (n==5){
                six+=1;
            }else if (n==6){
                seven+=1;
            }
        }
        String strDate=DATE_FORMAT.format(begin);
        for (int i=0;i<7;i++){
            KnowledgeViews  knowledgeViews=new KnowledgeViews();
            if (i==0){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),7)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(one);
            }else if(i==1){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),6)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(two);
            }else if(i==2){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),5)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(three);
            }else if(i==3){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),4)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(four);
            }else if(i==4){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),3)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(five);
            }else if(i==5){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),2)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(six);
            }else if(i==6){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),1)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(seven);
            }
            knowledgeViewses.add(knowledgeViews);
        }
        return knowledgeViewses;
    }

    public List<KnowledgeViews> knowledgeNumByMonth(List<Knowledge> knowledgeList,Date begin,int t){
        int num1,num2,num3,num4,num5,num6,num7,num8,num9,num10,num11,num12,num13,num14,num15,num16,num17,num18,num19,num20,num21,num22,num23,num24,num25,num26,num27,num28,num29,num30;
        num1=num2=num3=num4=num5=num6=num7=num8=num9=num10=num11=num12=num13=num14=num15=num16=num17=num18=num19=num20=num21=num22=num23=num24=num25=num26=num27=num28=num29=num30=0;
        String time=null;
        List<KnowledgeViews> knowledgeViewsList=new ArrayList<>();
        for (Knowledge knowledge:knowledgeList){
            Date date=knowledge.getCreatedTime();
            int n= (int) ((date.getTime()-begin.getTime())/(24*60*60*1000));
            if (n==0){
                num1+=1;
            }else if (n==1){
                num2+=1;
            }else if (n==2){
                num3+=1;
            }else if (n==3){
                num4+=1;
            }else if (n==4){
                num5+=1;
            }else if (n==5){
                num6+=1;
            }else if (n==6){
                num7+=1;
            }else if (n==7){
                num8+=1;
            }else if (n==8){
                num9+=1;
            }else if (n==8){
                num9+=1;
            }else if (n==9){
                num10+=1;
            }else if (n==10){
                num11+=1;
            }else if (n==11){
                num12+=1;
            }else if (n==12){
                num13+=1;
            }else if (n==13){
                num14+=1;
            }else if (n==14){
                num15+=1;
            }else if (n==15){
                num16+=1;
            }else if (n==16){
                num17+=1;
            }else if (n==17){
                num18+=1;
            }else if (n==18){
                num19+=1;
            }else if (n==19){
                num20+=1;
            }else if (n==20){
                num21+=1;
            }else if (n==21){
                num22+=1;
            }else if (n==22){
                num23+=1;
            }else if (n==23){
                num24+=1;
            }else if (n==24){
                num25+=1;
            }else if (n==25){
                num26+=1;
            }else if (n==26){
                num27+=1;
            }else if (n==27){
                num28+=1;
            }else if (n==28){
                num29+=1;
            }else if (n==29){
                num30+=1;
            }
        }
        String strDate=DATE_FORMAT.format(begin);
        for (int i=0;i<30;i++){
            KnowledgeViews knowledgeViews=new KnowledgeViews();
            if (i==0){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),30)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num1);
            }else if(i==1){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),29)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num2);
            }else if(i==2){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),28)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num3);
            }else if(i==3){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),27)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num4);
            }else if(i==4){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),26)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num5);
            }else if(i==5){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),25)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num6);
            }else if(i==6){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),24)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num7);
            }else if(i==7){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),23)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num8);
            }else if(i==8){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),22)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num9);
            }else if(i==9){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),21)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num10);
            }else if(i==10){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),20)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num11);
            }else if(i==11){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),19)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num12);
            }else if(i==12){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),18)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num13);
            }else if(i==13){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),17)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num14);
            }else if(i==14){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),16)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num15);
            }else if(i==15){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),15)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num16);
            }else if(i==16){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),14)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num17);
            }else if(i==17){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),13)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num18);
            }else if(i==18){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),12)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num19);
            }else if(i==19){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),11)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num20);
            }else if(i==20){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),10)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num21);
            }else if(i==21){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),9)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num22);
            }else if(i==22){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),8)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num23);
            }else if(i==23){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),7)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num24);
            }else if(i==24){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),6)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num25);
            }else if(i==25){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),5)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num26);
            }else if(i==26){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),4)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num27);
            }else if(i==27){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),3)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num28);
            }else if(i==28){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),2)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num29);
            }else if(i==29){
                time=DATE_FORMAT.format(Utils.getDayStart(Utils.daysBefore(new Date(),1)));
                knowledgeViews.setTime(time);
                knowledgeViews.setKnowledgeNum(num30);
            }
            knowledgeViewsList.add(knowledgeViews);
        }
        return knowledgeViewsList;
    }
}
