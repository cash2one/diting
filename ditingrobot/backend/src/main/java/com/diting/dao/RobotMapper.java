package com.diting.dao;

import com.diting.model.Robot;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * RobotMapper.
 */
public interface RobotMapper {

    void create(Robot robot);

    void update(Robot robot);

    Robot get(@Param("robotId") Integer robotId);

    Robot getByUserId(@Param("userId") Integer userId);

    Robot getByUniqueId(String unique_id);

    Robot getByDomainName(String domainName);

    boolean checkNameExists(@Param("robotId") Integer robotId, @Param("name") String name);

    boolean checkDomainNameExists(@Param("robotId") Integer robotId, @Param("domainName") String domainName);

    List<Robot> getByNameOrUniqueId(@Param("keyWord") String keyWord,@Param("companyId")Integer companyId);

    List<Robot> getClaimRobots();

    void updateIntelCalendar(@Param("robotId") Integer robotId);

    List<Robot> getByName(@Param("name") String name);

    List<Robot> getByRobotName(@Param("name") String name);

    List<Robot> getTelByRobotName(@Param("name") String name);

    List<Robot> getTelByCompanyName(@Param("name") String name);

}
