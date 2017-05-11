package com.diting.elasticsearch;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.client.Client;

/**
 * Created by liufei on 2016/6/16.
 */
public class DelIndex {

    private static Client client;

    public DelIndex() {
        client = ClientInit.getClient();
    }

    // 清除所有索引
    public void deleteIndex() {
        System.out.println("start process delete index!!!");
        IndicesExistsResponse indicesExistsResponse = client.admin().indices()
                .exists(new IndicesExistsRequest(new String[]{"diting"}))
                .actionGet();
        if (indicesExistsResponse.isExists()) {
            System.out.println("begin delete index!!!");
            client.admin().indices().delete(new DeleteIndexRequest("diting"))
                    .actionGet();
            System.out.println("end delete index!!!");
        }
        System.out.println("start process delete index!!!");
    }

    // 删除Index下的某个Type
    public void deleteType(String indexName, String typeName) {
        System.out.println("start process delete type!!!");
        TypesExistsResponse typeExistsResponse = client.admin().indices()
                .typesExists(new TypesExistsRequest(new String[]{indexName}, new String[]{typeName}))
                .actionGet();
        if(typeExistsResponse.isExists()){
            System.out.println("begin delete type!!!");
//            List<Medicine> medicines = searchAllForType(indexName,typeName);
//            for (Medicine medicine : medicines){
//                delete(indexName,typeName,medicine.getId().toString());
//            }
            System.out.println("end delete type!!!");
        }
        System.out.println("start process delete type!!!");
    }

    public static void delIndex(){
        DelIndex delIndex=new DelIndex();
//        delIndex.init();
        delIndex.deleteIndex();
//        delIndex.close();
    }

    public static void main(String[] args){
        DelIndex delIndex=new DelIndex();
//        delIndex.init();
        delIndex.deleteIndex();
//        delIndex.close();
    }
}
