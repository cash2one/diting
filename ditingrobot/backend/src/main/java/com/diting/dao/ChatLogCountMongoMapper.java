package com.diting.dao;

import com.diting.dao.mongo.BaseMongoMapper;
import com.diting.model.mongo.ChatLog;
import com.diting.util.Utils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by liufei on 2016/10/19.
 */
public class ChatLogCountMongoMapper extends BaseMongoMapper<ChatLog> {

    public Integer findChatLogCountByUsername(String username){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        return Math.toIntExact(mongoTemplate.count(query, modelClass));
    }

    public Integer findChatLogYesterdayCountByUsername(String username){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username).and("createdTime").gte(Utils.daysBefore(Utils.today(), 1)).lte(Utils.today()));
//        Criteria criteria = Criteria.where("createdTime").gte(Utils.daysBefore(Utils.today(), 1)).lte(Utils.today()).and("username").is("13552141271");
        return Math.toIntExact(mongoTemplate.count(query, modelClass));
    }

    public Integer findChatLogInvalidCountByUsername(String username){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username).and("extra1").is("0"));
        return Math.toIntExact(mongoTemplate.count(query, modelClass));
    }
}
