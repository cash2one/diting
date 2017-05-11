package com.diting.service;

import com.diting.model.Robot;

import java.util.List;

/**
 * RobotService.
 */
public interface RobotService {
    Robot create(Robot robot);

    Robot save(Robot robot);

    Robot update(Robot robot);

    Robot get(Integer robotId);

    Robot getByUserId(Integer userId);

    Robot getByUniqueId(String unique_id);

    Robot getByDomainName(String domainName);

    List<Robot> getClaimRobots();
}
