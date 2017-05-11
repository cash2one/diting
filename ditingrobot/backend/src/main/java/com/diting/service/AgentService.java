package com.diting.service;

import com.diting.model.Agent;
import com.diting.model.options.AgentOptions;
import com.diting.model.result.Results;

/**
 * AgentService.
 */
public interface AgentService {
    Agent register(Agent agent);

    Agent create(Agent agent);

    Agent update(Agent agent);

    Agent get(Integer agentId);

    Agent login(Agent agent);

    boolean checkMobileExists(String mobile);

    boolean checkUsernameExists(String userName);

    Results<Agent> searchForPage(AgentOptions options);

}
