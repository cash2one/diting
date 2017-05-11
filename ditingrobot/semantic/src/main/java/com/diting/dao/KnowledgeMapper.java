package com.diting.dao;

import com.diting.model.Knowledge;
import com.diting.model.options.KnowledgeOptions;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * KnowledgeMapper.
 */
public interface KnowledgeMapper {

     void create(Knowledge knowledge);

     void update(Knowledge knowledge);

     boolean batchUpdate(List<Knowledge> list);

     boolean batchUpdateKeywords(List<Knowledge> list);

     List<Knowledge> searchKnowledgeNum(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

     boolean delete(@Param("knowledgeId")Integer knowledgeId);

     Knowledge get(@Param("knowledgeId")Integer knowledgeId);

     List<Knowledge> getKnowledge(Knowledge knowledge);

     List<Knowledge> findKnowledgesByAccountId(@Param("accountId") Integer accountId);

     Knowledge getLastKnowledge();

     List<Knowledge> searchForPage(KnowledgeOptions options);

     List<Knowledge> companyKnowledgeList(KnowledgeOptions options);

     void updateFrequency(@Param("knowledgeId")Integer knowledgeId);

     List<Knowledge> searchCompanyKnowledgeForPage(KnowledgeOptions options);

     List<Knowledge> getKnowledgeList();

     Integer searchKnowledgeCount(KnowledgeOptions options);

     List<Knowledge> getKnowledgeByFrequencr(Knowledge knowledge);

     int searchKnowledgeCountByAccountId(KnowledgeOptions options);

}
