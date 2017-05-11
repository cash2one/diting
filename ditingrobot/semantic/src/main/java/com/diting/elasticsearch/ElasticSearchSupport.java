package com.diting.elasticsearch;


import com.diting.model.Knowledge;
import com.diting.model.options.ESSearchOptions;

import java.util.List;

/**
 * ElasticSearchSupport
 */
public class ElasticSearchSupport implements ElasticSearch {

    public List<Knowledge> search(String scj, String skw1, String skw2, String skw3, String skw4, String skw5, String type) {
        throw new RuntimeException("Not implemented");
    }

    public List<Knowledge> search(ESSearchOptions options) {
        throw new RuntimeException("Not implemented");
    }
    public List<Knowledge> searchByHandleQuestion(ESSearchOptions options) {
        throw new RuntimeException("Not implemented");
    }

    public List<Knowledge> searchByQuestionAndScene(ESSearchOptions options) {
        throw new RuntimeException("Not implemented");
    }

    public List<Knowledge> searchByQuestion(ESSearchOptions options) {
        throw new RuntimeException("Not implemented");
    }

//    @Override
    public List<Knowledge> searchByQuestionAndAnswer(Knowledge knowledge) {
        throw new RuntimeException("Not implemented");
    }

    public void create(Integer knowledgeId) {
        throw new RuntimeException("Not implemented");
    }

    public void delete(Integer knowledgeId) {
        throw new RuntimeException("Not implemented");
    }

    public void close() {
        throw new RuntimeException("Not implemented");
    }

}
