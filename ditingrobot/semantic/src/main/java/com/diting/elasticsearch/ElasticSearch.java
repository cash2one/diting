package com.diting.elasticsearch;

import com.diting.model.Knowledge;
import com.diting.model.options.ESSearchOptions;

import java.util.List;

/**
 * ElasticSearch
 */
public interface ElasticSearch {

    List<Knowledge> search(String scj, String skw1, String skw2, String skw3, String skw4, String skw5, String type);

    List<Knowledge> search(ESSearchOptions options);

    List<Knowledge> searchByHandleQuestion(ESSearchOptions options);

    List<Knowledge> searchByQuestionAndScene(ESSearchOptions options);

    List<Knowledge> searchByQuestion(ESSearchOptions options);

    List<Knowledge> searchByQuestionAndAnswer(Knowledge knowledge);

    void create(Integer knowledgeId);

    void delete(Integer knowledgeId);

    void close();

}
