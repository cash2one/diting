package com.diting.dao.mongo;

import com.diting.core.Universe;
import com.diting.dao.*;
import com.diting.model.*;
import com.diting.model.mongo.ChatLog;
import com.diting.model.options.ChatLogOptions;
import com.diting.model.result.Results;
import com.diting.util.Utils;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.diting.util.Utils.isEmpty;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * ChatLogMapper
 */
@SuppressWarnings("ALL")
public class ChatLogMapper extends BaseMongoMapper<ChatLog> {

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    RobotMapper robotMapper;

    @Autowired
    RegistrationUserMapper registrationUserMapper;

    @Autowired
    FansMapper fansMapper;

    public List<ChatLog> search(ChatLogOptions options) {
        Query query = new Query();
        if (!isEmpty(options.getUserName())) {
            query.addCriteria(Criteria.where("username").is(options.getUserName()));
        }
        return findByQuery(query);
    }

    public Results<ChatLog> searchForPage(ChatLogOptions options) {
        Query query = new Query();
        if (!isEmpty(options.getUuid())) {
            query.addCriteria(Criteria.where("uuid").is(options.getUuid()));
            query.addCriteria(Criteria.where("username").is(Universe.current().getUserName()));
        }
        Pageable pageable = new PageRequest(options.getPageNo() - 1, options.getPageSize(), new Sort(Sort.Direction.DESC, "_id"));
        return findPageable(query, pageable, new Sort(Sort.Direction.DESC, "_id"));
    }

    public Results<ChatLog> searchGroupForPage(ChatLogOptions options) {
        Criteria criteria = Criteria.where("username").is(options.getUserName());
        Results<BasicDBObject> chatLogResults = findGroupForPage(criteria, "uuid", options.getReduceFunction(), options.getPageNo(), options.getPageSize());
        Results<ChatLog> chatLogResults1 = new Results<>();
        List<BasicDBObject> chatLogs = chatLogResults.getItems();
        List<ChatLog> chatLogList = new ArrayList<>();
        for (int n = 0; n < chatLogs.size(); n++) {
            ChatLog chatLog = new ChatLog();
            BasicDBObject basicDBObject = chatLogs.get(n);
            chatLog.setUuid(basicDBObject.getString("uuid"));
            chatLog.setCount((int) basicDBObject.getDouble("count"));
            chatLog.setExtra4(basicDBObject.getString("extra4"));
            chatLog.setCreatedTime(basicDBObject.getDate("createdTime"));
            chatLog.setLoginUsername(basicDBObject.getString("loginUsername"));
            if (!isEmpty(basicDBObject.getString("loginUsername"))) {
                Account account = accountMapper.getByUsername(basicDBObject.getString("loginUsername"));
                if (null != account) {
                    chatLog.setHeadImgUrl(account.getHeadImgUrl());
                    Robot robot = robotMapper.getByUserId(account.getId());
                    if (null != robot) {
                        chatLog.setRobotName(robot.getName());
                        chatLog.setWelcome(robot.getWelcomes());
                        chatLog.setProfile(robot.getProfile());
                    }
                }
            }
            chatLogList.add(chatLog);

        }
        chatLogResults1.setItems(chatLogList);
        chatLogResults1.setPageNo(chatLogResults.getPageNo());
        chatLogResults1.setPageSize(chatLogResults.getPageSize());
        chatLogResults1.setTotal(chatLogResults.getTotal());
        chatLogResults1.setTotalPage(chatLogResults.getTotalPage());
        chatLogResults1.setTotalRecord(chatLogResults.getTotalRecord());
        return chatLogResults1;
    }

    public List<ChatLog> getAllRank() {
        List<ChatLog> chatLogs = new ArrayList<>();
        TypedAggregation<ChatLog> agg = Aggregation.newAggregation(
                ChatLog.class,
                project("username")
                , group("username").count().as("totalNum")
                , sort(Sort.Direction.DESC, "totalNum")
        );
        List<BasicDBObject> result = getAggregate(agg);
        for (BasicDBObject dbo : result) {
            ChatLog chatLog = new ChatLog();
            String username = dbo.getString("_id");
            if (username == null) {
                continue;
            }
            int num = dbo.getInt("totalNum");
            chatLog.setUsername(username);
            chatLog.setNum(num);
            Account account = accountMapper.getByMobile(username);
            if (account != null) {
                Company company = companyMapper.get(account.getCompanyId());
                Robot robot = robotMapper.getByUserId(account.getId());
                chatLog.setCompanyName(company.getName());
                chatLog.setApp_key(robot.getUniqueId());
                chatLog.setRobotName(robot.getName());
            } else {
                continue;
            }
            chatLogs.add(chatLog);
            if (chatLogs.size() >= 20) {
                break;
            }
        }
        return chatLogs;
    }

    public List<ChatLog> getTopFiftyRanking() {
        Integer userId = Universe.current().getUserId();
        List<Fans> fanses = fansMapper.search_fans_top();
        List<ChatLog> chatLogs = new ArrayList<>();
        for (Fans fans : fanses) {
            ChatLog chatLog = new ChatLog();
            chatLog.setUserId(Integer.valueOf(fans.getOwn_phone()));
            chatLog.setRobotName(fans.getRobot_name());
            chatLog.setCompanyName(fans.getCompany_name());
            Robot robot = robotMapper.getByUserId(Integer.valueOf(fans.getOwn_phone()));
            chatLog.setNum(Integer.valueOf(fans.getCc()));
            if (null != userId && !isEmpty(fans.getOth_phone()) && fans.getOth_phone().equals(String.valueOf(userId))) {
                chatLog.setAttentionState("true");
            } else {
                chatLog.setAttentionState("false");
            }
            if (null != robot && !isEmpty(robot.getUniqueId())) {
                chatLog.setApp_key(robot.getUniqueId());
            } else {
                continue;
            }
            if (chatLogs.size() >= 20)
                break;
            chatLogs.add(chatLog);
        }
//        TypedAggregation<ChatLog> agg = Aggregation.newAggregation(
//                ChatLog.class,
//                project("username", "createdTime")
//                , match(Criteria.where("createdTime").gte(Utils.str_Date("2017-01-01 00:00:00")))
//                , group("username").count().as("totalNum")
//                , sort(Sort.Direction.DESC, "totalNum")
//        );
//        List<BasicDBObject> result = getAggregate(agg);
//        for (BasicDBObject dbo : result) {
//            ChatLog chatLog = new ChatLog();
//            String username = dbo.getString("_id");
//            boolean bl=false;
//            for (int i=0;i<fanses.size();i++){
//                Account account=accountMapper.get(Integer.valueOf(fanses.get(i).getOwn_phone()));
//                String account_name=null!=account?account.getUserName():null;
//                if (username.equals(account_name)){
//                    bl=true;
//                }
//                if (i>=20){
//                    break;
//                }
//            }
//            if (bl){
//                List<RegistrationUser> registrationUserList = registrationUserMapper.search();
//                if (username == null) {
//                    continue;
//                }
//                Boolean isBoolean = false;
//                if (null != registrationUserList && registrationUserList.size() > 0) {
//                    for (RegistrationUser registrationUser : registrationUserList) {
//                        if (registrationUser.getUserName().equals(username)) {
//                            isBoolean = true;
//                            continue;
//                        }
//                    }
//                }
//                if (!isBoolean) {
//                    continue;
//                }
//                int num = dbo.getInt("totalNum");
//                chatLog.setUsername(username);
//                chatLog.setNum(num);
//                Account account = accountMapper.getByMobile(username);
//                if (account != null) {
//                    Company company = companyMapper.get(account.getCompanyId());
//                    Robot robot = robotMapper.getByUserId(account.getId());
//                    chatLog.setCompanyName(company.getName());
//                    chatLog.setApp_key(robot.getUniqueId());
//                    chatLog.setRobotName(robot.getName());
//                } else {
//                    continue;
//                }
//                chatLogs.add(chatLog);
//                System.out.println(username+"   "+chatLog.getUsername()+"   "+chatLog.getNum());
////                if (chatLogs.size() >= 50) {
////                    break;
////                }
//            }
//        }
        return chatLogs;
    }

    public List<ChatLog> getYesterdayRank() {
        List<ChatLog> chatLogs = new ArrayList<>();
        Integer userId = Universe.current().getUserId();
        TypedAggregation<ChatLog> agg = Aggregation.newAggregation(
                ChatLog.class,
                project("username", "createdTime")
                , match(Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), 1)).lte(Utils.today()))
                , group("username").count().as("totalNum")
                , sort(Sort.Direction.DESC, "totalNum")
        );
        List<BasicDBObject> result = getAggregate(agg);
        for (BasicDBObject dbo : result) {
            ChatLog chatLog = new ChatLog();
            String username = dbo.getString("_id");
            if (username == null) {
                continue;
            }
            int num = dbo.getInt("totalNum");
            chatLog.setUsername(username);
            chatLog.setNum(num);
            Account account = accountMapper.getByMobile(username);
            if (account != null) {
                Company company = companyMapper.get(account.getCompanyId());
                Robot robot = robotMapper.getByUserId(account.getId());
                if (null == company || null == company.getName() || robot == null) {
                    continue;
                } else {
                    chatLog.setCompanyName(company.getName());
                    chatLog.setApp_key(robot.getUniqueId());
                    chatLog.setRobotName(robot.getName());
                    chatLog.setWelcome(robot.getWelcomes());
                    chatLog.setProfile(robot.getProfile());
                    chatLog.setHeadImgUrl(account.getHeadImgUrl());
                    if (isEmpty(company.getName()) || isEmpty(robot.getName())) {
                        continue;
                    }
                }
            } else {
                continue;
            }
            if (null != userId) {
                Fans fans = fansMapper.searchFansByUserId(account.getId(), userId);
                chatLog.setAttentionState(fans == null ? "false" : "true");
            }
            chatLog.setUserId(account.getId());
            chatLogs.add(chatLog);
            if (chatLogs.size() >= 20) {
                break;
            }
        }
        return chatLogs;

//        Criteria criteria = Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), 1)).lte(Utils.today());
//        Results<ChatLog> result = findGroupForPage(criteria, "username", "function(doc, prev){prev.count+=1,prev.extra4=doc.extra4,prev.createdTime=doc.createdTime}", 1, 100);
//        JSONArray json = new JSONArray();
//        json.addAll(result.getItems());
//        for (int i = 0; i < json.size(); i++) {
//            ChatLog chatLog = new ChatLog();
//            String username = json.getJSONObject(i).getString("username");
//            if (username == null) {
//                continue;
//            }
//            int num = json.getJSONObject(i).getInteger("count");
//            chatLog.setUsername(username);
//            chatLog.setNum(num);
//            Account account = accountMapper.getByMobile(username);
//            if (null!=account) {
//                Company company = companyMapper.get(account.getCompanyId());
//                Robot robot = robotMapper.getByUserId(account.getId());
//                if (null==company||null==company.getName()||robot==null){
//                    continue;
//                }else {
//                    chatLog.setCompanyName(company.getName());
//                    chatLog.setApp_key(robot.getUniqueId());
//                }
//            }else {
//                continue;
//            }
//            chatLogs.add(chatLog);
//            if (chatLogs.size() >= 20) {
//                break;
//            }
//        }
//        return chatLogs;
    }

    public void updateByUsername(String oldValue, String newValue) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(oldValue));
        Update update = new Update();
        update.set("username", newValue);
        updateByUsername(query, update);
        System.out.println("Document updated successfully");
    }

    public long searchDayValidCount() {
        Query query = new Query();
        query.addCriteria(Criteria.where("extra1").is("0"));
        Criteria criteria = Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), 1)).lt(Utils.today());
        query.addCriteria(criteria);
        return mongoTemplate.count(query, modelClass);
    }

    public long searchDayInvalidCount() {
        Query query = new Query();
        query.addCriteria(Criteria.where("extra1").is("1"));
        Criteria criteria = Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), 1)).lt(Utils.today());
        query.addCriteria(criteria);
        return mongoTemplate.count(query, modelClass);
    }

    public long searchDayQuestionCount() {
        Query query = new Query();
        Criteria criteria = Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), 1)).lt(Utils.today());
        query.addCriteria(criteria);
        return mongoTemplate.count(query, modelClass);
    }

    public long searchDayQuestionCountByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        query.addCriteria(Criteria.where("chare_mode").is("普通模式"));
        Criteria criteria = Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), 1)).lt(Utils.today());
        query.addCriteria(criteria);
        return mongoTemplate.count(query, modelClass);
    }

    public int searchQuestionAndAnswerNumber() {
        TypedAggregation<ChatLog> agg = Aggregation.newAggregation(
                ChatLog.class,
                project("uuid", "createdTime")
                , match(Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), 1)).lte(Utils.today()))
                , group("uuid").count().as("totalNum")
                , sort(Sort.Direction.DESC, "totalNum")
        );
        List<BasicDBObject> result = getAggregate(agg);
        return result.size();
    }

    public int searchQuestionAndAnswerUserNum() {
        TypedAggregation<ChatLog> agg = Aggregation.newAggregation(
                ChatLog.class,
                project("username", "createdTime")
                , match(Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), 1)).lte(Utils.today()))
                , group("username").count().as("totalNum")
                , sort(Sort.Direction.DESC, "totalNum")
        );
        List<BasicDBObject> result = getAggregate(agg);
        return result.size();
    }

    public List<ChatLog> searchChatLogByChargeMode(String username) {

        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        query.addCriteria(Criteria.where("charge_mode").is("专家模式"));
        query.addCriteria(Criteria.where("extra1").is("0"));
        Criteria criteria = Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), 1)).lt(Utils.today());
        query.addCriteria(criteria);
        List<ChatLog> list = mongoTemplate.find(query, modelClass);
        return list;
    }

    public int searchYesterdayVisitorsNum() {
        TypedAggregation<ChatLog> agg = Aggregation.newAggregation(
                ChatLog.class,
                project("uuid", "createdTime", "username")
                , match(Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), 1)).lte(Utils.today()).and("username").is(Universe.current().getUserName()))
                , group("uuid").count().as("totalNum")
                , sort(Sort.Direction.DESC, "totalNum")
        );
        List<BasicDBObject> result = getAggregate(agg);
        return result.size();
    }

    public int getYesterdayAskNum() {
        TypedAggregation<ChatLog> agg = Aggregation.newAggregation(
                ChatLog.class,
                project("uuid", "createdTime", "username")
                , match(Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), 1)).lte(Utils.today()).and("username").is(Universe.current().getUserId()))
        );
        List<BasicDBObject> result = getAggregate(agg);
        return result.size();
    }

    public int getYesterdayUnknownQuestionNum() {
        TypedAggregation<ChatLog> agg = Aggregation.newAggregation(
                ChatLog.class,
                project("uuid", "createdTime", "extra1", "username")
                , match(Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), 1)).lte(Utils.today()).and("username").is(Universe.current().getUserId()).and("extra1").is("1"))
        );
        List<BasicDBObject> result = getAggregate(agg);
        return result.size();
    }

    public long searchAllQuestionCountByUsername(String username) {
        Query query = new Query();
        Criteria criteria = Criteria.where("username").is(username);
        query.addCriteria(criteria);
        return mongoTemplate.count(query, modelClass);
    }

    public int searchAllCountGroupByUUIDWhereByUsername(String username) {
        TypedAggregation<ChatLog> agg = Aggregation.newAggregation(
                ChatLog.class,
                project("uuid", "username")
                , match(Criteria.where("username").is(username))
                , group("uuid").count().as("totalNum")
        );
        List<BasicDBObject> result = getAggregate(agg);
        return result.size();
    }

    public int searchAllCountGroupByUUIDWhereByUsernameAndTime(String username, Date beginTime, Date endTime) {
        TypedAggregation<ChatLog> agg = Aggregation.newAggregation(
                ChatLog.class,
                project("uuid", "username", "createdTime")
                , match(Criteria.where("username").is(username).and("createdTime").gt(beginTime).lte(endTime))
                , group("uuid").count().as("totalNum")
        );
        List<BasicDBObject> result = getAggregate(agg);
        return result.size();
    }

    public int searchAllCountByUsernameAndTime(String username, Date beginTime, Date endTime) {
        TypedAggregation<ChatLog> agg = Aggregation.newAggregation(
                ChatLog.class,
                project("uuid", "username", "createdTime")
                , match(Criteria.where("username").is(username).and("createdTime").gt(beginTime).lte(endTime))
        );
        List<BasicDBObject> result = getAggregate(agg);
        return result.size();
    }

    public long searchCountByExtra4AndTime(String extra1, String extra4, Date beginTime, Date endTime) {
        Query query = new Query();
        if (extra1 != null && extra1 != "") {
            query.addCriteria(Criteria.where("extra1").is(extra1));
        }
        if (extra4 != null && extra4 != "") {
            query.addCriteria(Criteria.where("extra4").is(extra4));
        }
        if (beginTime != null && endTime != null) {
            query.addCriteria(Criteria.where("createdTime").gte(beginTime).lt(endTime));
        }
        return mongoTemplate.count(query, modelClass);
    }

    public List<ChatLog> searchChatLogByCreatedTime(String username, int days) {

        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        Criteria criteria = Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), days)).lt(Utils.today());
        query.addCriteria(criteria);
        List<ChatLog> list = mongoTemplate.find(query, modelClass);
        return list;
    }

    public List<ChatLog> searchAllChatLogByCreatedTime() {

        Query query = new Query();
        Criteria criteria = Criteria.where("createdTime").gte(Utils.today());
        query.addCriteria(criteria);
        List<ChatLog> list = mongoTemplate.find(query, modelClass);
        return list;
    }

    public BasicDBList searchQuestionAndAnswerUserByTime(int type) {
        List<ChatLog> chatLogs = new ArrayList<>();
        Criteria criteria = Criteria.where("createdTime").gte(Utils.today());
        if (type>0)
            criteria = Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), type)).lt(Utils.today());
        BasicDBList basicDBList = findGroup(criteria, "uuid", "function(doc, prev){prev.count+=1,prev.extra4=doc.extra4,prev.createdTime=doc.createdTime,prev.loginUsername=doc.loginUsername,prev.username=doc.username}");
        for (int i = 0; i < basicDBList.size(); i++) {
            ChatLog chatLog = new ChatLog();
            BasicDBObject object = (BasicDBObject) basicDBList.get(i);
            object.get("createdTime");
            object.getDate("createdTime");
            System.out.println(basicDBList.get(i).getClass());
        }
        return basicDBList;
    }
    public BasicDBList searchAnswerUserByTime(int type) {
        List<ChatLog> chatLogs = new ArrayList<>();
        Criteria criteria = Criteria.where("createdTime").gte(Utils.today());
        if (type>0)
            criteria = Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), type)).lt(Utils.today());
        BasicDBList basicDBList = findGroup(criteria, "username", "function(doc, prev){prev.count+=1,prev.extra4=doc.extra4,prev.createdTime=doc.createdTime,prev.loginUsername=doc.loginUsername,prev.username=doc.username}");
        for (int i = 0; i < basicDBList.size(); i++) {
            ChatLog chatLog = new ChatLog();
            BasicDBObject object = (BasicDBObject) basicDBList.get(i);
            object.get("createdTime");
            object.getDate("createdTime");
            System.out.println(basicDBList.get(i).getClass());
        }
        return basicDBList;
    }

    public List<ChatLog> searchAllChatLogByCreatedTime(int days) {

        Query query = new Query();
        Criteria criteria = Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), days)).lt(Utils.today());
        query.addCriteria(criteria);
        List<ChatLog> list = mongoTemplate.find(query, modelClass);
        return list;
    }

    public BasicDBList searchGroupByUuidAndUsername(String username, int days) {
        List<ChatLog> chatLogs = new ArrayList<>();
        Criteria criteria = Criteria.where("username").is(username).and("createdTime").gte(Utils.daysBefore(Utils.today(), days)).lt(Utils.today());
        BasicDBList basicDBList = findGroup(criteria, "uuid", "function(doc, prev){prev.count+=1,prev.extra4=doc.extra4,prev.createdTime=doc.createdTime,prev.loginUsername=doc.loginUsername,prev.username=doc.username}");
        for (int i = 0; i < basicDBList.size(); i++) {
            ChatLog chatLog = new ChatLog();
            BasicDBObject object = (BasicDBObject) basicDBList.get(i);
            object.get("createdTime");
            object.getDate("createdTime");
            System.out.println(basicDBList.get(i).getClass());
        }
        return basicDBList;
    }

}
