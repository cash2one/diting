package com.diting.dao;

import com.diting.model.Agent;
import com.diting.model.options.AgentOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AgentMapper.
 */
public interface AgentMapper {

    void create(Agent account);

    void update(Agent account);

    Agent get(@Param("agentId") Integer agentId);

    Agent getByUserName(@Param("userName") String userName);

    Agent checkUsernameLogin(@Param("userName")String userName, @Param("password")String password);

    boolean checkMobileExists(@Param("mobile") String mobile);

    boolean checkUsernameExists(@Param("userName") String userName);

    List<Agent> searchForPage(AgentOptions options);

}
