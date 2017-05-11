package com.diting.service.impl.love;

import com.diting.core.Universe;
import com.diting.dao.RobotMapper;
import com.diting.dao.love.LoveKnowledgeMapper;
import com.diting.model.Robot;
import com.diting.model.love.LoveKnowledge;
import com.diting.service.love.LoveKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.diting.util.Utils.isEmpty;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * Created by shenkun on 2017/3/6.
 */
@SuppressWarnings("ALL")
@Service("loveKnowledgeService")
@Transactional
public class LoveKnowledgeImpl implements LoveKnowledgeService{
    @Autowired
    private LoveKnowledgeMapper loveKnowledgeMapper;
    @Autowired
    private RobotMapper robotMapper;

    @Override
    public LoveKnowledge create(LoveKnowledge loveKnowledge) {
        //获取当前登录机器人及用户信息
        if(Universe.current().getUserId()!=null){
            loveKnowledge.setUserId(Universe.current().getUserId());
            loveKnowledge.setUserName(Universe.current().getUserName());
            loveKnowledge.setCreatedBy(Universe.current().getUserName());
            //根据当前用户id获取机器人
            Robot robot=robotMapper.getByUserId(Universe.current().getUserId());
            if(robot!=null){
                loveKnowledge.setRobotId(robot.getId());
                loveKnowledge.setRobotName(robot.getName());
                loveKnowledge.setUniqueId(robot.getUniqueId());
            }
            loveKnowledgeMapper.create(loveKnowledge);
        }
        return loveKnowledge;
    }

    @Override
    public List<LoveKnowledge> findAll() {
            return loveKnowledgeMapper.findAll();
    }

    @Override
    public LoveKnowledge selectById(Integer id) {
        return loveKnowledgeMapper.selectById(id);
    }
}
