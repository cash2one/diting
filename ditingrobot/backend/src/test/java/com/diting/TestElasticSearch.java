package com.diting;

//import com.diting.elasticsearch.ElasticSearch;
//import com.diting.model.Knowledge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import java.util.List;

/**
 * TestElasticSearch.
 */
public class TestElasticSearch extends BaseTest {
    @Autowired
    @Qualifier("elasticSearchService")
//    private ElasticSearch elasticSearchService;

    @Test(enabled = false)
    public void testBVT() {
        try {
//            List<Knowledge> allKnowledge = elasticSearchService.search("刘德华", "刘德华", null, null, null, null, "tb_knowledge");
//            System.out.println(allKnowledge.size());
//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
