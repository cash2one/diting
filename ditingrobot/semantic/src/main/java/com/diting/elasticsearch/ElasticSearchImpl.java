package com.diting.elasticsearch;


import com.alibaba.fastjson.JSONObject;
import com.diting.dao.KnowledgeMapper;
import com.diting.model.Knowledge;
import com.diting.model.options.ESSearchOptions;
import com.diting.util.Utils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.shield.ShieldPlugin;
import org.elasticsearch.shield.authc.support.SecuredString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import static com.diting.util.Utils.*;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.shield.authc.support.UsernamePasswordToken.basicAuthHeaderValue;

/**
 * ElasticSearchImpl
 */
@SuppressWarnings("ALL")
public class ElasticSearchImpl extends ElasticSearchSupport implements InitializingBean {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final String CLUSTER_NAME = "diting";

    @Autowired
    private KnowledgeMapper knowledgeMapper;

    @Value("${es.index.name}")
    private String indexName;

    @Value("${es.service.ip1}")
    private String service1;

    @Value("${es.service.ip2}")
    private String service2;

    private static Client client;

    private static IndexRequestBuilder requestBuilder;

    private static Settings settings = Settings.settingsBuilder()
            .put("cluster.name", CLUSTER_NAME)
            .put("client.transport.sniff", true)
            .put("client.transport.ping_timeout", "100s")
            .put("shield.user","diting123456:diting123456")
            .build();


    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            if (Utils.equals(service1, service2)) {
                client = TransportClient.builder()
                        .addPlugin(ShieldPlugin.class)
                        .settings(settings).build()
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(service1), 9300));
                String token = basicAuthHeaderValue("diting123456",
                        new SecuredString("diting123456".toCharArray()));
                client.prepareSearch().putHeader("Authorization", token).get();
            } else {
                client = TransportClient.builder()
                        .addPlugin(ShieldPlugin.class)
                        .settings(settings)
                        .build()
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(service1), 9300))
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(service2), 9300));
                String token = basicAuthHeaderValue("diting123456",
                        new SecuredString("diting123456".toCharArray()));
                client.prepareSearch().putHeader("Authorization", token).get();
            }
        } catch (Exception e) {
            LOGGER.info("error occurred during init setting. " + e.getMessage());
        }
    }

    public List<Knowledge> search(String scj, String skw1, String skw2, String skw3, String skw4, String skw5, String type) {
        List<Knowledge> list = new ArrayList<>();//"你们","你们","公司","叫","什么","kz","13991358085"
        SearchResponse response = null;
        QueryBuilder queryBuilder = null;
        if (scj != null && skw1 != null && skw2 != null && skw3 != null && skw4 != null && skw5 == null) {//kw5 is null
            queryBuilder = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("scene", scj.trim()))
                    .must(QueryBuilders.termQuery("kw1", skw1.trim()))
                    .must(QueryBuilders.termQuery("kw2", skw2.trim()))
                    .must(QueryBuilders.termQuery("kw3", skw3.trim()))
                    .must(QueryBuilders.termQuery("kw4", skw4.trim()));

        } else if (scj != null && skw1 != null && skw2 != null && skw3 != null && skw4 == null && skw5 == null) {  //kw4 and kw5 is null
            queryBuilder = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("scene", scj.trim()))
                    .must(QueryBuilders.termQuery("kw1", skw1.trim()))
                    .must(QueryBuilders.termQuery("kw2", skw2.trim()))
                    .must(QueryBuilders.termQuery("kw3", skw3.trim()));
        } else if (scj != null && skw1 != null && skw2 != null && skw3 != null && skw4 != null && skw5 != null) {   //not is null
            queryBuilder = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("scene", scj.trim()))
                    .must(QueryBuilders.termQuery("kw1", skw1.trim()))
                    .must(QueryBuilders.termQuery("kw2", skw2.trim()))
                    .must(QueryBuilders.termQuery("kw3", skw3.trim()))
                    .must(QueryBuilders.termQuery("kw4", skw4.trim()))
                    .must(QueryBuilders.termQuery("kw5", skw5.trim()));
        } else if (scj == null && skw1 != null && skw2 != null && skw3 != null && skw4 != null && skw5 != null) { //cj is null
            queryBuilder = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("kw1", skw1.trim()))
                    .must(QueryBuilders.termQuery("kw2", skw2.trim()))
                    .must(QueryBuilders.termQuery("kw3", skw3.trim()))
                    .must(QueryBuilders.termQuery("kw4", skw4.trim()))
                    .must(QueryBuilders.termQuery("kw5", skw5.trim()));
        } else if (scj != null && skw1 != null && skw2 != null && skw3 != null && skw4 == null && skw5 == null) {  //kw4 and kw5 is null
            queryBuilder = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("scene", scj.trim()))
                    .must(QueryBuilders.termQuery("kw1", skw1.trim()))
                    .must(QueryBuilders.termQuery("kw2", skw2.trim()))
                    .must(QueryBuilders.termQuery("kw3", skw3.trim()));
        } else if (scj == null && skw1 != null && skw2 != null && skw3 != null && skw4 != null && skw5 == null) {//cj and kw5 is null
            queryBuilder = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("kw1", skw1.trim()))
                    .must(QueryBuilders.termQuery("kw2", skw2.trim()))
                    .must(QueryBuilders.termQuery("kw3", skw3.trim()))
                    .must(QueryBuilders.termQuery("kw4", skw4.trim()));
        } else if (scj == null && skw1 != null && skw2 != null && skw3 != null && skw4 == null && skw5 == null) {//cj and kw5 is null
            queryBuilder = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("kw1", skw1.trim()))
                    .must(QueryBuilders.termQuery("kw2", skw2.trim()))
                    .must(QueryBuilders.termQuery("kw3", skw3.trim()));
        }else if (scj != null && skw1 == null && skw2 == null && skw3 == null && skw4 == null && skw5 == null) {//cj and kw5 is null
            queryBuilder = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("scene", scj.trim()));
        }
        response = client.prepareSearch("diting1")
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();
        for (int i = 0; i < hits.getHits().length; i++) {
            String str = hits.getHits()[i].getSourceAsString();
            JSONObject jsonObject = JSONObject.parseObject(str);
            Knowledge knowledge = knowledgeAdd(jsonObject);
            list.add(knowledge);
            System.out.println("类型1：" + type + ":   " + hits.getHits()[i].getSourceAsString());
            if (i > 30) {
                break;
            }
        }
        return list;
    }

    public List<Knowledge> searchByQuestion(ESSearchOptions options) {
        QueryBuilder queryBuilder = null;
        if (null!=options.getSkw1()&&null!=options.getSkw2()&&null!=options.getSkw3()&&null!=options.getSkw4()&&null!=options.getSkw5()){
            queryBuilder = QueryBuilders.boolQuery()
                    .must(matchPhraseQuery("question_analyzed", options.getSkw1()))
                    .must(matchPhraseQuery("question_analyzed", options.getSkw2()))
                    .must(matchPhraseQuery("question_analyzed", options.getSkw3()))
                    .must(matchPhraseQuery("question_analyzed", options.getSkw4()))
                    .must(matchPhraseQuery("question_analyzed", options.getSkw5()))
                    .must(termQuery("scene", options.getScj()));
        }else if (null!=options.getSkw1()&&null!=options.getSkw2()&&null!=options.getSkw3()&&null!=options.getSkw4()&&null==options.getSkw5()){
            queryBuilder = QueryBuilders.boolQuery()
                    .must(matchPhraseQuery("question_analyzed", options.getSkw1()))
                    .must(matchPhraseQuery("question_analyzed", options.getSkw2()))
                    .must(matchPhraseQuery("question_analyzed", options.getSkw3()))
                    .must(matchPhraseQuery("question_analyzed", options.getSkw4()))
                    .must(termQuery("scene", options.getScj()));
        }else if (null!=options.getSkw1()&&null!=options.getSkw2()&&null!=options.getSkw3()&&null==options.getSkw4()&&null==options.getSkw5()){
            queryBuilder = QueryBuilders.boolQuery()
                    .must(matchPhraseQuery("question_analyzed", options.getSkw1()))
                    .must(matchPhraseQuery("question_analyzed", options.getSkw2()))
                    .must(matchPhraseQuery("question_analyzed", options.getSkw3()))
                    .must(termQuery("scene", options.getScj()));
        }else if (null!=options.getSkw1()&&null!=options.getSkw2()&&null==options.getSkw3()&&null==options.getSkw4()&&null==options.getSkw5()){
            queryBuilder = QueryBuilders.boolQuery()
                    .must(matchPhraseQuery("question_analyzed", options.getSkw1()))
                    .must(matchPhraseQuery("question_analyzed", options.getSkw2()))
                    .must(termQuery("scene", options.getScj()));
        }else if (null!=options.getSkw1()&&null==options.getSkw2()&&null==options.getSkw3()&&null==options.getSkw4()&&null==options.getSkw5()){
            queryBuilder = QueryBuilders.boolQuery()
                    .must(matchPhraseQuery("question_analyzed", options.getSkw1()))
                    .must(termQuery("scene", options.getScj()));
        }

        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(options.getType())
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();

        List<Knowledge> list = new ArrayList<>();

        for (int i = 0; i < hits.getHits().length; i++) {
            String str = hits.getHits()[i].getSourceAsString();
            JSONObject jsonObject = JSONObject.parseObject(str);
            Knowledge knowledge = knowledgeAdd(jsonObject);
            list.add(knowledge);
            System.out.println("类型1：  " + hits.getHits()[i].getSourceAsString());
            if (i > 30) {
                break;
            }
        }
        return list;
    }

    @Override
    public List<Knowledge> searchByQuestionAndAnswer(Knowledge options) {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("question", options.getQuestion()))
                    .must(QueryBuilders.termQuery("answer", options.getAnswer()))
                    .must(QueryBuilders.termQuery("scene", options.getScene()));

        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(String.valueOf(options.getCompanyId()))
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();

        List<Knowledge> list = new ArrayList<>();

        for (int i = 0; i < hits.getHits().length; i++) {
            String str = hits.getHits()[i].getSourceAsString();
            JSONObject jsonObject = JSONObject.parseObject(str);
            Knowledge knowledge = knowledgeAdd(jsonObject);
            list.add(knowledge);
            System.out.println("ES检验数据是否重复：  " + hits.getHits()[i].getSourceAsString());
            if (i > 30) {
                break;
            }
        }
        return list;
    }

    public List<Knowledge> search(ESSearchOptions options) {
        QueryBuilder queryBuilder = null;
        if (null!=options.getSkw1()&&null!=options.getSkw2()&&null!=options.getSkw3()&&null!=options.getSkw4()&&null!=options.getSkw5()){
            queryBuilder = QueryBuilders.boolQuery()
                    .must(matchPhraseQuery("answer_analyzed", options.getSkw1()))
                    .must(matchPhraseQuery("answer_analyzed", options.getSkw2()))
                    .must(matchPhraseQuery("answer_analyzed", options.getSkw3()))
                    .must(matchPhraseQuery("answer_analyzed", options.getSkw4()))
                    .must(matchPhraseQuery("answer_analyzed", options.getSkw5()))
                    .must(termQuery("scene", options.getScj()));
        }else if (null!=options.getSkw1()&&null!=options.getSkw2()&&null!=options.getSkw3()&&null!=options.getSkw4()&&null==options.getSkw5()){
            queryBuilder = QueryBuilders.boolQuery()
                    .must(matchPhraseQuery("answer_analyzed", options.getSkw1()))
                    .must(matchPhraseQuery("answer_analyzed", options.getSkw2()))
                    .must(matchPhraseQuery("answer_analyzed", options.getSkw3()))
                    .must(matchPhraseQuery("answer_analyzed", options.getSkw4()))
                    .must(termQuery("scene", options.getScj()));
        }else if (null!=options.getSkw1()&&null!=options.getSkw2()&&null!=options.getSkw3()&&null==options.getSkw4()&&null==options.getSkw5()){
            queryBuilder = QueryBuilders.boolQuery()
                    .must(matchPhraseQuery("answer_analyzed", options.getSkw1()))
                    .must(matchPhraseQuery("answer_analyzed", options.getSkw2()))
                    .must(matchPhraseQuery("answer_analyzed", options.getSkw3()))
                    .must(termQuery("scene", options.getScj()));
        }else if (null!=options.getSkw1()&&null!=options.getSkw2()&&null==options.getSkw3()&&null==options.getSkw4()&&null==options.getSkw5()){
            queryBuilder = QueryBuilders.boolQuery()
                    .must(matchPhraseQuery("answer_analyzed", options.getSkw1()))
                    .must(matchPhraseQuery("answer_analyzed", options.getSkw2()))
                    .must(termQuery("scene", options.getScj()));
        }else if (null!=options.getSkw1()&&null==options.getSkw2()&&null==options.getSkw3()&&null==options.getSkw4()&&null==options.getSkw5()){
            queryBuilder = QueryBuilders.boolQuery()
                    .must(matchPhraseQuery("answer_analyzed", options.getSkw1()))
                    .must(termQuery("scene", options.getScj()));
        }

        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(options.getType())
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();

        List<Knowledge> list = new ArrayList<>();

        for (int i = 0; i < hits.getHits().length; i++) {
            String str = hits.getHits()[i].getSourceAsString();
            JSONObject jsonObject = JSONObject.parseObject(str);
            Knowledge knowledge = knowledgeAdd(jsonObject);
            list.add(knowledge);
            System.out.println("类型1：  " + hits.getHits()[i].getSourceAsString());
            if (i > 30) {
                break;
            }
        }
        return list;
    }

    public List<Knowledge> searchByHandleQuestion(ESSearchOptions options) {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("h_question", options.getHandleQuestion()))
                .must(QueryBuilders.termQuery("scene", options.getScj()));

        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(options.getType())
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();

        List<Knowledge> list = new ArrayList<>();

        for (int i = 0; i < hits.getHits().length; i++) {
            String str = hits.getHits()[i].getSourceAsString();
            JSONObject jsonObject = JSONObject.parseObject(str);
            Knowledge knowledge = knowledgeAdd(jsonObject);
            list.add(knowledge);
//            System.out.println("类型1：  " + hits.getHits()[i].getSourceAsString());
            if (i > 30) {
                break;
            }
        }
        return list;
    }

    public List<Knowledge> searchByQuestionAndScene(ESSearchOptions options) {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("question", options.getQuestion()))
                .must(QueryBuilders.termQuery("scene", options.getScj()));

        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(options.getType())
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();

        List<Knowledge> list = new ArrayList<>();

        for (int i = 0; i < hits.getHits().length; i++) {
            String str = hits.getHits()[i].getSourceAsString();
            JSONObject jsonObject = JSONObject.parseObject(str);
            Knowledge knowledge = knowledgeAdd(jsonObject);
            list.add(knowledge);
//            System.out.println("类型1：  " + hits.getHits()[i].getSourceAsString());
            if (i > 30) {
                break;
            }
        }
        return list;
    }

    public void create(Integer knowledgeId) {
        Knowledge knowledge = knowledgeMapper.get(knowledgeId);
        createIndex(indexName);
        requestBuilder = createIndexMapping(indexName, str(knowledge.getCompanyId()));
        index(requestBuilder, knowledge);
    }

    public void delete(Integer knowledgeId) {
        Knowledge knowledge = knowledgeMapper.get(knowledgeId);
        remove(indexName, str(knowledge.getCompanyId()), str(knowledge.getId()));
    }

    public void close() {
        client.close();
    }

    private void createIndex(String indexName) {
        IndicesExistsResponse indicesExistsResponse = client.admin().indices()
                .exists(new IndicesExistsRequest(new String[]{indexName}))
                .actionGet();
        if (!indicesExistsResponse.isExists()) {
            client.admin().indices().create(new CreateIndexRequest(indexName))
                    .actionGet();
        }
    }

    private IndexRequestBuilder createIndexMapping(String indexName, String indexType) {
        //创建索引库 需要注意的是.setRefresh(true)这里一定要设置,否则第一次建立索引查找不到数据
        IndexRequestBuilder requestBuilder = client.prepareIndex(indexName, indexType).setRefresh(true);
        try {
            XContentBuilder content = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(indexType)
                    .startObject("properties")
                    .startObject("id")
                    .field("type", "integer")
                    .endObject()
                    .startObject("company_id")
                    .field("type", "integer")
                    .endObject()
                    .startObject("account_id")
                    .field("type", "integer")
                    .endObject()
                    .startObject("question")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("question_analyzed")
                    .field("type", "string")
                    .endObject()
                    .startObject("h_question")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("handle_question_analyzed")
                    .field("type", "string")
                    .endObject()
                    .startObject("answer")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("answer_analyzed")
                    .field("type", "string")
                    .endObject()
                    .startObject("kw1")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("kw2")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("kw3")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("kw4")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("kw5")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("scene")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("emotion")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("action_option")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .endObject()
                    .endObject()
                    .endObject();
            //省略读取mapping文件的java代码，内容保存在mapping_json中。
            client.admin().indices().preparePutMapping(indexName).setType(indexType).setSource(content).execute().actionGet();
        } catch (Exception e) {
            LOGGER.info("error occurred during create mapping." + e.getMessage());
        }
        return requestBuilder;
    }

    private void index(IndexRequestBuilder requestBuilder, Knowledge knowledge) {
        try {
            requestBuilder.setId(str(knowledge.getId())).setSource(parseJson2Str(knowledge)).execute().actionGet();
        } catch (Exception e) {
            LOGGER.info("error occurred during index knowledge [" + knowledge.getId() + "]." + e.getMessage());
        }
    }

    private void remove(String indexName, String indexType, String id) {
        try {
            client.prepareDelete(indexName, indexType, id).get();
            LOGGER.info("successful delete index type [" + indexType + "] and id [" + id + "]");
        } catch (Exception e) {
            LOGGER.info("error occurred during delete type [" + indexType + "] and id [" + id + "]." + e.getMessage());
        }
    }

    private static String parseJson2Str(Knowledge knowledge) {
        String json = "";
        try {
            XContentBuilder contentBuilder = XContentFactory.jsonBuilder()
                    .startObject();
            contentBuilder.field("id", knowledge.getId() + "");
            contentBuilder.field("company_id", knowledge.getCompanyId() + "");
            contentBuilder.field("account_id", knowledge.getAccountId() + "");
            contentBuilder.field("question", knowledge.getQuestion());
            contentBuilder.field("question_analyzed", knowledge.getQuestion());
            contentBuilder.field("h_question", knowledge.getHandleQuestion());
            contentBuilder.field("handle_question_analyzed", knowledge.getHandleQuestion());
            contentBuilder.field("answer", knowledge.getAnswer());
            contentBuilder.field("answer_analyzed", knowledge.getAnswer());
            contentBuilder.field("kw1", knowledge.getKw1().replace("$", ""));
            contentBuilder.field("kw2", knowledge.getKw2().replace("$", ""));
            contentBuilder.field("kw3", knowledge.getKw3().replace("$", ""));
            contentBuilder.field("kw4", knowledge.getKw4().replace("$", ""));
            contentBuilder.field("kw5", knowledge.getKw5().replace("$", ""));
            contentBuilder.field("scene", knowledge.getScene());
            contentBuilder.field("emotion", knowledge.getEmotion());
            contentBuilder.field("action_option", knowledge.getActionOption());
            json = contentBuilder.endObject().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    private static Knowledge knowledgeAdd(JSONObject jsonObject) {
        String id = jsonObject.getString("id");
        String question = jsonObject.getString("question");
        String answer = jsonObject.getString("answer");
        String kw1 = jsonObject.getString("kw1");
        String kw2 = jsonObject.getString("kw2");
        String kw3 = jsonObject.getString("kw3");
        String kw4 = jsonObject.getString("kw4");
        String kw5 = jsonObject.getString("kw5");
        String cj = jsonObject.getString("scene");
        Integer emotion = str2int(isNull(jsonObject.getString("emotion"), "5"));
        Knowledge knowledge = new Knowledge();
        knowledge.setId(Integer.valueOf(id));
        knowledge.setQuestion(question);
        knowledge.setAnswer(answer);
        knowledge.setKw1(kw1);
        knowledge.setKw2(kw2);
        knowledge.setKw3(kw3);
        knowledge.setKw4(kw4);
        knowledge.setKw5(kw5);
        knowledge.setEmotion(emotion);
        knowledge.setScene(cj);
        return knowledge;
    }
}
