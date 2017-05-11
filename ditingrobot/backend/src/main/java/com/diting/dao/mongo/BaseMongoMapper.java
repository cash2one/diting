package com.diting.dao.mongo;

import com.diting.model.mongo.BaseMongoModel;
import com.diting.model.mongo.MongoSequence;
import com.diting.model.options.PageableOptions;
import com.diting.model.result.Results;
import com.diting.util.Utils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;


/**
 * BaseMongoMapper.
 */
public class BaseMongoMapper<T extends BaseMongoModel> implements MongoSequenceMapper {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected static final String ID = "_id";
    protected static final Integer DEFAULT_START = 0;
    protected static final Integer DEFAULT_COUNT = 15;
    protected static final String DEFAULT_ORDER = "asc";
    protected static final String DEFAULT_ORDER_BY = "_id";

    protected MongoTemplate mongoTemplate;

    protected DBCollection collection;

    protected Class<T> modelClass;

    @Value("${mongo.enabled}")
    private boolean mongoEnable;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        if (mongoEnable) afterPropertySet();
    }

    public void create(T entity) {
        if (mongoEnable) {
            mongoTemplate.insert(fullEntity(entity));
        }
    }

    public T get(Integer id) {
        Query query = new Query().addCriteria(Criteria.where(ID).is(id));
        return mongoTemplate.findOne(query, modelClass);
    }

    public void update(Integer id, T entity) {
        throw new RuntimeException("not supported");
    }

    public void delete(Integer id) {
        Query query = new Query().addCriteria(Criteria.where(ID).is(id));
        mongoTemplate.findAndRemove(query, modelClass);
    }

    public boolean exists(Integer id) {
        return get(id) != null;
    }

    public List<T> getAll() {
        Query query = new Query();
        return mongoTemplate.find(query, modelClass);
    }

    public Results<T> getAllByPagination(PageableOptions options) {
        throw new RuntimeException("not supported");
    }

    public List<T> batchGet(List<Integer> ids) {
        Query query = new Query().addCriteria(Criteria.where(ID).in(ids));
        return mongoTemplate.find(query, modelClass);
    }

    public List<String> getAutoSuggestNames() {
        throw new RuntimeException("not supported");
    }

    public void batchCreate(List<T> entities) {
        if (!mongoEnable) return;
        List<T> list = new ArrayList<T>();
        for (T entity : entities) {
            list.add(fullEntity(entity));
        }
        mongoTemplate.insertAll(list);
    }

    public void bulkCreate(@Param("entities") List<T> entities) {
        throw new RuntimeException("not supported yet");
    }

    public void batchUpdate(List<T> entities) {
        throw new RuntimeException("not supported yet");
    }

    public void updateByUsername(Query query,Update update){
        mongoTemplate.updateMulti(query,update,modelClass);
    }

    public List<T> findByQuery(Query query) {
        return mongoTemplate.find(query, modelClass);
    }

    public T findOneByQuery(Query query) {
        if (modelClass == null) {
            Type t = getClass().getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] p = ((ParameterizedType) t).getActualTypeArguments();
                modelClass = (Class<T>) p[0];
            }
        }
        return mongoTemplate.findOne(query, modelClass);
    }

    public Results<T> findPageable(Query query, Pageable pageable, Sort sortable) {
        Long title = mongoTemplate.count(query, modelClass);
        List<T> entities = findByQuery(query.with(pageable).with(sortable));
        Results results = new Results();
        results.setItems(entities);
        results.setTotal(title.intValue());
        return results;
    }

    public BasicDBList findGroup(Criteria criteria, String groupKey, String reduceFunction) {
        if (Utils.isEmpty(reduceFunction)) {
            reduceFunction = "function(doc, prev){prev.count+=1}";
        }

        GroupBy groupBy = GroupBy.key(groupKey).initialDocument("{count:0}")
                .reduceFunction(reduceFunction);
        GroupByResults<T> r = mongoTemplate.group(criteria, getModelClassName().toLowerCase(), groupBy, modelClass);
        return (BasicDBList) r.getRawResults().get("retval");
    }

    public Results<BasicDBObject> findGroupForPage(Criteria criteria, String groupKey, String reduceFunction, Integer pageNo, Integer pageSize) {
        BasicDBList list = findGroup(criteria, groupKey, reduceFunction);

        // only used for statistics
        sort(list);

        Integer total = list.size();
        Integer from = (pageNo - 1) * pageSize;
        Integer to = pageNo * pageSize;

        if (total < to) {
            to = total;
        }

        Results results = new Results();
        results.setItems(list.subList(from, to));
        results.setTotal(total);
        return results;
    }

    @Override
    public Integer getNextSequenceId() {
        //get sequence id
        Query query = new Query(Criteria.where("collection").is(getModelClassName()));

        //increase sequence id by 1
        Update update = new Update();
        update.inc("value", 1);

        //return new increased id
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);

        //this is the magic happened.
        MongoSequence sequence =
                mongoTemplate.findAndModify(query, update, options, MongoSequence.class);

        if (sequence == null) {
            throw new RuntimeException("Failed to generate id for the model [" + getModelClassName() + "].");

        }

        return sequence.getValue();
    }

    private BasicDBList sort(BasicDBList list) {
        Collections.sort(list, new Comparator<Object>() {
            public int compare(Object obj1, Object obj2) {
                return ((Date) (((BasicDBObject) obj2).get("createdTime"))).compareTo(((Date) (((BasicDBObject) obj1).get("createdTime"))));
            }
        });
        return list;
    }

    public Class<T> getModelClass() {
        return modelClass;
    }

    public String getModelClassName() {
        return modelClass.getSimpleName();
    }

    public Pageable pageable(Integer start, Integer count) {
        start = Utils.isNull(start, DEFAULT_START);
        count = Utils.isNull(count, DEFAULT_COUNT);
        return new PageRequest(start / count, count);
    }

    public Sort sortable(String order, String orderBy) {
        order = Utils.isNull(order, DEFAULT_ORDER);
        orderBy = Utils.isNull(orderBy, DEFAULT_ORDER_BY);
        if (Utils.equals(order, "asc")) {
            return new Sort(Sort.Direction.ASC, orderBy);
        } else {
            return new Sort(Sort.Direction.DESC, orderBy);
        }
    }

    protected T fullEntity(T entity) {
        entity.setId(getNextSequenceId());
        if (entity.getCreatedTime() == null) {
            entity.setCreatedTime(new Date());
        }
        if (entity.getUpdatedTime() == null) {
            entity.setUpdatedTime(new Date());
        }
        return entity;
    }

    /**
     * 初始化modelclass, collection, sequence
     */
    private void afterPropertySet() {
        modelClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        collection = mongoTemplate.getCollection(mongoTemplate.getCollectionName(modelClass));
        initializeSequence();
    }

    /**
     * 初始化sequence
     */
    private void initializeSequence() {
        Query query = new Query(Criteria.where("collection").is(getModelClassName()));
        MongoSequence sequence = mongoTemplate.findOne(query, MongoSequence.class);
        if (sequence == null) {
            try {
                MongoSequence newSeq = new MongoSequence();
                newSeq.setCollection(getModelClassName());
                newSeq.setValue(1);
                mongoTemplate.insert(newSeq);
            } catch (Exception e) {
                LOGGER.warn("Failed to initialize sequence for model [" + getModelClassName() + "].");
            }
        }
    }

    public List<BasicDBObject> getAggregate(TypedAggregation typedAggregation){
        AggregationResults<BasicDBObject> result = mongoTemplate.aggregate(typedAggregation, BasicDBObject.class);
        return result.getMappedResults();
    }
}
