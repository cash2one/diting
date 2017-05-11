package com.diting.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.diting.model.Knowledge;
//import net.sf.json.JSONObject;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by liufei on 2016/6/27.
 */
public class EditIndex {

    private static Client client;

    public EditIndex() {
        client = ClientInit.getClient();
    }

    public void updateIndexResponse(String indexType, Knowledge knowledge) {
        //创建索引库 需要注意的是.setRefresh(true)这里一定要设置,否则第一次建立索引查找不到数据
            try {
                client.prepareUpdate("diting1", indexType, String.valueOf(knowledge.getId()))
                        .setDoc(jsonBuilder()
                                .startObject()
                                .field("question", knowledge.getQuestion())
                                .field("answer", knowledge.getAnswer())
                                .field("kw1", knowledge.getKw1().replace("$",""))
                                .field("kw2", knowledge.getKw2().replace("$",""))
                                .field("kw3", knowledge.getKw3().replace("$",""))
                                .field("kw4", knowledge.getKw4().replace("$",""))
                                .field("kw5", knowledge.getKw5().replace("$",""))
                                .field("scene", knowledge.getScene())
                                .field("company_id", knowledge.getCompanyId())
                                .field("account_id", knowledge.getAccountId())
                                .field("emotion", knowledge.getEmotion())
                                .endObject())
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    //根据id查询索引文件
    public void findById(String parameter){
        List<Knowledge> list=new ArrayList<>();
        SearchResponse response=null;
        QueryBuilder queryBuilder=null;
        queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("id", parameter));
        response = client.prepareSearch("diting1")
                .setTypes("51")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits());
        for (int i = 0; i < hits.getHits().length; i++) {
            String str=hits.getHits()[i].getSourceAsString();
            JSONObject jsonObject= JSON.parseObject(str);
//            Knowledge knowledge=knowledgeAdd(jsonObject);
//            list.add(knowledge);
//            System.out.println(hits.getHits()[i].getSourceAsString());
            System.out.println(jsonObject);
        }
    }
    public void delete(String indexType,String id){
        try {
            DeleteResponse response = client.prepareDelete("diting1", indexType, id).get();
            System.out.println("successful delete id:"+response.getId());
        }catch (Exception e ){
            System.out.println(e.getMessage());
        }
    }
    public void insert(){
        List<String> list= new ArrayList<>();
        Knowledge knowledge = new Knowledge();
        knowledge.setId(202621);
        knowledge.setQuestion("难道你是321");
        knowledge.setAnswer("我真不想死，你就让我活着吧231");
        knowledge.setKw1("231");
        knowledge.setKw2("你1");
        knowledge.setKw3("kz231");
        knowledge.setKw4("kz123");
        knowledge.setKw5("kz13");
        knowledge.setEmotion(5);
        knowledge.setScene("场景132");
        list.add(generateJson(knowledge));
        CreateIndex createIndex=new CreateIndex();
//        createIndex.createIndexResponse("dtrobot",list);
    }
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
    public static void main(String[] args) {
        EditIndex editIndex = new EditIndex();
        editIndex.findById("362818");
//        editIndex.insert();
//        editIndex.delete();
//        editIndex.updateIndexResponse(null,null,null);
    }
}
