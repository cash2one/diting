package com.diting.service.impl.love;

import com.diting.core.Universe;
import com.diting.dao.AccountMapper;
import com.diting.dao.RobotMapper;
import com.diting.dao.love.LoveMessageLogMapper;
import com.diting.model.Account;
import com.diting.model.Robot;
import com.diting.model.love.LoveMessageLog;
import com.diting.service.love.LoveMessageLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by shenkun on 2017/3/10.
 */
@SuppressWarnings("ALL")
@Service("loveMessageLogService")
@Transactional
public class LoveMessageLogImpl implements LoveMessageLogService{
    @Autowired
    private LoveMessageLogMapper loveMessageLogMapper;
    @Autowired
    private RobotMapper robotMapper;
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public List<LoveMessageLog> selectByUserId() {
        if(Universe.current().getUserId()!=null){
            return loveMessageLogMapper.selectByUserId(Universe.current().getUserId());
        }
        return null;
    }

    @Override
    public LoveMessageLog create(LoveMessageLog loveMessageLog) {
        if(Universe.current().getUserId()!=null) {
            loveMessageLog.setForwordId(Universe.current().getUserId());
            //获取当前账号机器人信息
            Robot robot = robotMapper.getByUserId(Universe.current().getUserId());
            loveMessageLog.setForwordName(robot.getName());
            //获取当前聊天机器人信息(将uniqueid暂时存放在receiveName中)
            Robot robotChat = robotMapper.getByUniqueId(loveMessageLog.getReceiveName());
            loveMessageLog.setReceiveId(robotChat.getAccountId());
            loveMessageLog.setReceiveName(robotChat.getName());
            Account account=accountMapper.get(robotChat.getAccountId());
            loveMessageLog.setReceiveHeadImgUrl(account.getHeadImgUrl());
            loveMessageLogMapper.create(loveMessageLog);
            return loveMessageLog;
        }
        return null;
    }

    @Override
    public Integer selectCountByUserId() {
        return loveMessageLogMapper.selectCountByUserId(Universe.current().getUserId());
    }

    @Override
    public void updateByUserId() {
        loveMessageLogMapper.updateByUserId(Universe.current().getUserId());
    }

    @Override
    public Integer selectCountByLoveId(Integer loveId) {
        return loveMessageLogMapper.selectCountByLoveId(loveId);
    }

    @Override
    public List<Map<String, Object>> selectCountMap() {
        return loveMessageLogMapper.selectCountMap();
    }

}
