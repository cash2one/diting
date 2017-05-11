package com.diting.service.mongo;

import com.diting.error.AppErrors;
import com.diting.model.BaseModel;
import com.diting.model.mongo.MongoSequence;
import com.diting.util.Utils;
import com.mongodb.DBCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;

/**
 * BaseMongoServiceImpl
 */
@Transactional
public class BaseMongoServiceImpl<MODEL extends BaseModel> implements BaseMongoService<MODEL>, InitializingBean {
    private static final String ID = "_id";
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${mongo.enabled}")
    private boolean mongoEnable;

    @Autowired
    public MongoTemplate mongoTemplate;

    private DBCollection collection;

    private Class<MODEL> modelClass;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (mongoEnable) afterPropertySet();
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


    @Override
    public MODEL create(MODEL model) {
        Utils.checkNull(model, "entity");

        try {
            if (mongoEnable) {
                mongoTemplate.insert(fullEntity(model));
            }
        } catch (Exception e) {
            LOGGER.error("Failed to create entity.", e);

            throw AppErrors.INSTANCE.createFailed(getModelClassName()).exception();
        }

        return model;
    }

    @Override
    public MODEL get(Integer id) {
        Query query = new Query().addCriteria(Criteria.where(ID).is(id));
        return mongoTemplate.findOne(query, modelClass);
    }

    @Override
    public void delete(Integer id) {
        Query query = new Query().addCriteria(Criteria.where(ID).is(id));
        mongoTemplate.findAndRemove(query, modelClass);
    }

    @Override
    public boolean exists(Integer id) {
        return get(id) != null;
    }

    @Override
    public List<MODEL> getAll() {
        Query query = new Query();
        return mongoTemplate.find(query, modelClass);
    }

    /**
     * 初始化modelclass, collection, sequence
     */
    private void afterPropertySet() {
        modelClass = (Class<MODEL>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
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

    private MODEL fullEntity(MODEL entity) {
        entity.setId(getNextSequenceId());
        entity.setCreatedTime(new Date());
        entity.setUpdatedTime(new Date());
        return entity;
    }

    private String getModelClassName() {
        return modelClass.getSimpleName();
    }
}
