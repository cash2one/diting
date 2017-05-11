package com.diting.service;

import com.diting.model.BaseInfo;
import com.diting.model.Knowledge;
import com.diting.model.options.KnowledgeOptions;
import com.diting.model.result.Results;
import com.diting.model.views.KnowledgeViews;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/10.
 */
public interface KnowledgeService {

    Knowledge create(Knowledge knowledge);

    Knowledge getScene(Knowledge knowledge);

    Map<String,String> complexCreate(Knowledge knowledge);

    Map<String,String> complexTempCreate(Knowledge knowledge);

    Map<String,String> baseKnowledgeCreate(Knowledge knowledge);

    Knowledge update(Knowledge knowledge);

    Knowledge baseKnowledgeUpdate(Knowledge knowledge);

    Boolean delete(Integer knowledgeId);

    Knowledge get(Integer knowledgeId);

    Results<Knowledge> searchForPage(KnowledgeOptions options);

    Boolean batchDelete(String[] strings);

    Boolean adminBatchDelete(String[] strings);

    List<Knowledge>  findKnowledgesByAccountId(Integer accountId);

    Map batchInsert(List<Map> maps);

    Map baseBatchInsert(List<Map> maps);

    List<Knowledge> findCompanyKnowledgeList(KnowledgeOptions options);

    List<Knowledge> findBaseKnowledgeList(KnowledgeOptions options);

    Results<Knowledge> searchCompanyKnowledgeForPage(KnowledgeOptions options);

    void updateAllKeys();

    void createBaseKnowledgeInfo(BaseInfo baseInfo);

    BaseInfo getBaseInfoByCompanyId();

    List<KnowledgeViews> searchKnowledgeNum(String type);

    Integer searchKnowledgeCount(KnowledgeOptions options);

    List<Knowledge> getKnowledgeByFrequencr(Knowledge knowledge);

    int searchKnowledgeCountByAccountId(KnowledgeOptions options);
}
