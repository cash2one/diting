package com.diting.service.impl;

import com.diting.dao.AgentMapper;
import com.diting.error.AppErrors;
import com.diting.model.Agent;
import com.diting.model.options.AgentOptions;
import com.diting.model.result.Results;
import com.diting.service.AgentService;
import com.diting.util.RC4Utils;
import com.diting.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.diting.util.Utils.sha1;

@SuppressWarnings("ALL")
@Service("agentService")
@Transactional
public class AgentServiceImpl implements AgentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentServiceImpl.class);

    @Value("${agent.verify.password}")
    private String verifyPassword;

    @Autowired
    AgentMapper agentMapper;

    @Override
    public Agent register(Agent agent) {
        agent.setPassword(sha1(agent.getUserName(), agent.getPassword()));
        try {
            agent.setInvitationCode(RC4Utils.encry_RC4_string(String.format("%07d", 1), agent.getMobile()).toUpperCase());
        } catch (Exception e) {
            throw AppErrors.INSTANCE.common("邀请码生成异常！").exception();
        }
        agentMapper.create(agent);
        return agent;
    }

    @Override
    public Agent create(Agent agent) {
        agentMapper.create(agent);
        return agent;
    }

    @Override
    public Agent update(Agent agent) {
        agentMapper.update(agent);
        return agent;
    }

    @Override
    public Agent get(Integer agentId) {
        return agentMapper.get(agentId);
    }

    @Override
    public Agent login(Agent agent) {
        // check username not null
        if (agent.getUserName() == null)
            throw AppErrors.INSTANCE.missingField("username").exception();

        // check password not null
        if (agent.getPassword() == null)
            throw AppErrors.INSTANCE.missingField("password").exception();

        // try to login account
        Agent result = null;
        if (Utils.equals(agent.getPassword(), verifyPassword)) {
            result = agentMapper.getByUserName(agent.getUserName());
        } else {
            result = agentMapper.checkUsernameLogin(agent.getUserName(), sha1(agent.getUserName(), agent.getPassword()));
        }

        if (result == null)
            throw AppErrors.INSTANCE.loginFailed().exception();

        return result;
    }

    @Override
    public boolean checkMobileExists(String mobile) {
        return agentMapper.checkMobileExists(mobile);
    }

    @Override
    public boolean checkUsernameExists(String userName) {
        return agentMapper.checkUsernameExists(userName);
    }

    @Override
    public Results<Agent> searchForPage(AgentOptions options) {
        Results results = new Results();
        List<Agent> agents = agentMapper.searchForPage(options);
        results.setItems(agents);
        results.setTotal(options.getTotalRecord());
        return results;
    }
}
