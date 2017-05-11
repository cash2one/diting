package com.diting.dao.love;

import com.diting.model.love.LoveKnowledge;

import java.util.List;

/**
 * SkillStoreMapper.
 */
public interface LoveKnowledgeMapper {
    List<LoveKnowledge> findAll();
    void create(LoveKnowledge loveKnowledge);
    LoveKnowledge selectById(Integer id);
}
