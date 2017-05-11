package com.diting.service.love;

import com.diting.model.love.LoveKnowledge;

import java.util.List;

/**
 * Created by shenkun on 2017/3/6.
 */
public interface LoveKnowledgeService {
    LoveKnowledge create(LoveKnowledge loveKnowledge);
    List<LoveKnowledge> findAll();
    LoveKnowledge selectById(Integer id);
}
