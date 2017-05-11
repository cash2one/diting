package com.diting;

import com.diting.elasticsearch.ElasticSearch;
import com.diting.model.Knowledge;
import com.diting.model.options.ESSearchOptions;
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
    private ElasticSearch elasticSearchService;

    @Test(enabled = false)
    public void testBVT() {
        try {
            List<Knowledge> allKnowledge = elasticSearchService.search("你们", "你们", "产品", "使用", "上", "技能", "1");
            System.out.println(allKnowledge.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(enabled = false)
    public void testBVT1() {
        try {
            elasticSearchService.create(1);
            elasticSearchService.close();
            System.out.println("success create");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(enabled = false)
    public void testBVT2() {
        try {
            ESSearchOptions options = new ESSearchOptions();
            options.setScj("来得及");
            options.setSkw3("来得及");
            options.setType("216");
            List<Knowledge> allKnowledge = elasticSearchService.search(options);
            System.out.println(allKnowledge.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(enabled = false)
    public void testBVT3() {
        try {
          elasticSearchService.delete(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
