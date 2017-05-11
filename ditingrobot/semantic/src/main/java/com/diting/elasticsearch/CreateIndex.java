package com.diting.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.diting.util.DBHelper;
import com.diting.model.Knowledge;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;

/**
 * Created by liufei on 2016/6/14.
 */
public class CreateIndex {

    private static Client client;

    public CreateIndex() {
        client = ClientInit.getClient();
    }


    public void createIndex(String indexName) {
        IndicesExistsResponse indicesExistsResponse = client.admin().indices()
                .exists(new IndicesExistsRequest(new String[]{indexName}))
                .actionGet();
        if (!indicesExistsResponse.isExists()) {
            client.admin().indices().create(new CreateIndexRequest(indexName))
                    .actionGet();
        }
    }

    public void createIndexResponse(String indexType, List<String> jsonData) {
        try {
            //创建索引库 需要注意的是.setRefresh(true)这里一定要设置,否则第一次建立索引查找不到数据
            IndexRequestBuilder requestBuilder = client.prepareIndex("diting1", indexType).setRefresh(true);
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
                    .endObject()
                    .endObject()
                    .endObject();
            //省略读取mapping文件的java代码，内容保存在mapping_json中。
            createIndex("diting1");

            for (String aJsonData : jsonData) {
                JSONObject jsonObject = JSON.parseObject(aJsonData);

                client.admin().indices().preparePutMapping("diting1").setType(indexType).setSource(content).execute().actionGet();
//                IndexRequestBuilder requestBuilder = client.prepareIndex(indexName, jsonObject.get("company_id").toString()).setRefresh(true);
//                if (i%10000==0){
//                    System.out.println("第"+i+"次:"+jsonData.get(i));
//                }
                requestBuilder.setId(jsonObject.get("id").toString()).setSource(aJsonData).execute().actionGet();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createOneIndex(List<Knowledge> knowledgeList){
        List<String> list= new ArrayList<>();
        for (Knowledge aKnowledgeList : knowledgeList) {
            list.add(generateJson(aKnowledgeList));
        }
        //正式es服务器
        createIndexResponse(String.valueOf(knowledgeList.get(0).getCompanyId()),list);
//        测试es服务器
//        createIndexResponse("ditingrobot",String.valueOf(knowledgeList.get(0).getCompanyId()),list);
    }
    public void createIndexDB(Integer companyId) throws Exception {
        String tableName=null;
        String sql = "SELECT question,answer,id,kw1,kw2,kw3,kw4,kw5,scene,emotion,company_id,account_id from knowledge WHERE company_id =" + companyId+" and deleted="+0;
//        String sql = "SELECT question,answer,id,kw1,kw2,kw3,kw4,kw5,scene,emotion from " + tableName ;
        Connection conn = DBHelper.getConn();
        if (conn == null) {
            throw new Exception("数据库连接失败！");
        }
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<String> list= new ArrayList<>();
        while (rs.next()) {
            Knowledge knowledge = new Knowledge();
            knowledge.setId(rs.getInt("id"));
            knowledge.setQuestion(rs.getString("question"));
            knowledge.setAnswer(rs.getString("answer"));
            knowledge.setKw1(rs.getString("kw1").replace("$", ""));
            knowledge.setKw2(rs.getString("kw2").replace("$", ""));
            knowledge.setKw3(rs.getString("kw3").replace("$", ""));
            knowledge.setKw4(rs.getString("kw4").replace("$", ""));
            knowledge.setKw5(rs.getString("kw5").replace("$", ""));
            knowledge.setEmotion(rs.getInt("emotion"));
            knowledge.setScene(rs.getString("scene"));
            knowledge.setAccountId(rs.getInt("account_id"));
            knowledge.setCompanyId(rs.getInt("company_id"));
            list.add(generateJson(knowledge));
        }
        createIndexResponse(String.valueOf(companyId),list);
        stmt.close();
        rs.close();
    }
    /**
     * 转换成json对象
     *
     * @param knowledge
     * @return
     */
    private static String generateJson(Knowledge knowledge) {
        String json = "";
        try {
            XContentBuilder contentBuilder = XContentFactory.jsonBuilder()
                    .startObject();
            contentBuilder.field("id", knowledge.getId() + "");
            contentBuilder.field("question", knowledge.getQuestion());
            contentBuilder.field("question_analyzed", knowledge.getQuestion());
            contentBuilder.field("answer", knowledge.getAnswer() );
            contentBuilder.field("answer_analyzed", knowledge.getAnswer());
            contentBuilder.field("kw1", knowledge.getKw1().replace("$",""));
            contentBuilder.field("kw2", knowledge.getKw2().replace("$",""));
            contentBuilder.field("kw3", knowledge.getKw3().replace("$",""));
            contentBuilder.field("kw4", knowledge.getKw4().replace("$",""));
            contentBuilder.field("kw5", knowledge.getKw5().replace("$",""));
            contentBuilder.field("scene", knowledge.getScene());
            contentBuilder.field("emotion", knowledge.getEmotion());
            contentBuilder.field("account_id", knowledge.getAccountId());
            contentBuilder.field("company_id", knowledge.getCompanyId());
            json = contentBuilder.endObject().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public void searchIndex() {
        SearchResponse response = client.prepareSearch("diting1234")
                .setTypes("liufei")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                .setQuery(termQuery("question","你是谁呢"))// Query
//                .setQuery(termQuery("answer","我是你"))// Query
                .setQuery(matchPhraseQuery("question_analyzed","你是"))
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits());
        for (int i = 0; i < hits.getHits().length; i++) {
            System.out.println(hits.getHits()[i].getSourceAsString());
        }
    }

    public void addIndex(List<Knowledge> knowledgeList){
        CreateIndex client = new CreateIndex();
//        client.init();
        client.createOneIndex(knowledgeList);
//        client.close();
    }

    public static List<Integer> companyIds() throws Exception {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT id from company" ;
        Connection conn = DBHelper.getConn();
        if (conn == null) {
            throw new Exception("数据库连接失败！");
        }
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(rs.getInt("id"));
        }
        return list;
    }
    //给所有知识库创建索引
    public void createAllIndex(){
        try {
            List<Integer> list = companyIds();
            CreateIndex client = new CreateIndex();
//            client.init();
            System.out.print("开始创建索引...");
            for(int i=0;i<list.size();i++){
                Integer companyId=list.get(i);
                System.out.println(companyId+"    "+i);
                client.createIndexDB(companyId);
            }
//            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            List<Integer> list = companyIds();
            CreateIndex clientIndex = new CreateIndex();
//            client.init();
            System.out.print("开始创建索引...:"+list.size());
            clientIndex.createIndexDB(60);
//            for(int i=0;i<list.size();i++){
//                Integer companyId=list.get(i);
//                System.out.println(companyId+"    "+i);
//                clientIndex.createIndexDB(companyId);
//            }
//            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
