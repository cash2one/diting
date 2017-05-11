package com.diting.service.impl;

import com.diting.core.Universe;
import com.diting.dao.AccountMapper;
import com.diting.dao.ChatStatisticsMapper;
import com.diting.dao.CompanyMapper;
import com.diting.dao.RobotMapper;
import com.diting.error.AppErrors;
import com.diting.model.Account;
import com.diting.model.ChatStatistics;
import com.diting.model.Company;
import com.diting.model.Robot;
import com.diting.service.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.diting.util.Utils.generateToken;
import static com.diting.util.Utils.get32UUID;

@SuppressWarnings("ALL")
@Service("robotService")
@Transactional
public class RobotServiceImpl implements RobotService {


    @Autowired
    private RobotMapper robotMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ChatStatisticsMapper chatStatisticsMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public Robot create(Robot robot) {
        robotMapper.create(robot);
        return robot;
    }

    @Override
    public Robot save(Robot robot) {

        Robot result = robotMapper.getByUserId(Universe.current().getUserId());
        Account account = accountMapper.get(Universe.current().getUserId());

        if (null!=account.getCompanyId()){
            Company company=companyMapper.get(account.getCompanyId());
            if (companyMapper.checkNameExists(company.getId(), robot.getCompanyName()))
                throw AppErrors.INSTANCE.common("公司名称已存在！").exception();
            company.setIndustry(robot.getIndustry());
            company.setName(robot.getCompanyName());
            companyMapper.update(company);
        }else {
            if (companyMapper.checkNameExists(null, robot.getCompanyName()))
                throw AppErrors.INSTANCE.common("公司名称已存在！").exception();
            Company company=new Company();
            company.setName(robot.getCompanyName());
            company.setIndustry(robot.getIndustry());
            companyMapper.create(company);
            account.setCompanyId(company.getId());
            accountMapper.update(account);
        }

        if (result == null) {
            if (robotMapper.checkNameExists(null, robot.getName()))
                throw AppErrors.INSTANCE.common("机器人名称已存在！").exception();

            if (robotMapper.checkDomainNameExists(null, robot.getShortDomainName()))
                throw AppErrors.INSTANCE.common("短域名已存在！").exception();
            robot.setUniqueId(get32UUID());
            robot.setToken(generateToken(6));
            robot.setCompanyId(account.getCompanyId());
            robot.setAccountId(account.getId());
            robotMapper.create(robot);
        } else {
            if (robotMapper.checkNameExists(result.getId(), robot.getName()))
                throw AppErrors.INSTANCE.common("机器人名称已存在！").exception();

            if (robotMapper.checkDomainNameExists(result.getId(), robot.getShortDomainName()))
                throw AppErrors.INSTANCE.common("短域名已存在！").exception();

            robot.setId(result.getId());
            robotMapper.update(robot);
        }


        return robot;
    }

    @Override
    public Robot update(Robot robot) {
        robotMapper.update(robot);
        return robot;
    }

    @Override
    public Robot get(Integer robotId) {
        return robotMapper.get(robotId);
    }

    @Override
    public Robot getByUserId(Integer userId) {
        Robot robot=robotMapper.getByUserId(userId);
        if (robot!=null){
            Company company=companyMapper.get(robot.getCompanyId());
            if (null!=company){
                robot.setCompanyName(company.getName());
                robot.setIndustry(company.getIndustry());
            }
        }
        return robot;
    }

    @Override
    public Robot getByUniqueId(String unique_id) {
        Robot robot=robotMapper.getByUniqueId(unique_id);
        if (null!=robot){
            Account account=accountMapper.get(robot.getAccountId());
            if (null!=account){
                robot.setUsername(account.getUserName());
                robot.setHeadImg(account.getHeadImgUrl());
            }
        }
        return robot;
    }

    @Override
    public Robot getByDomainName(String domainName) {
        return robotMapper.getByDomainName(domainName);
    }

    @Override
    public List<Robot> getClaimRobots(){
        List<Robot> robotList=robotMapper.getClaimRobots();
        for (Robot robot:robotList){
            ChatStatistics chatStatistics=chatStatisticsMapper.findByCompanyId(robot.getCompanyId());
            if (null!=chatStatistics){
                robot.setRobotVal(String.valueOf(chatStatistics.getRobotValue()));
            }
        }
        return robotList;
    }


}
