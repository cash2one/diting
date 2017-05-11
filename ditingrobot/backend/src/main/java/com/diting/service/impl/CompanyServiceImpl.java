package com.diting.service.impl;

import com.diting.core.Universe;
import com.diting.dao.AccountMapper;
import com.diting.dao.ChatStatisticsMapper;
import com.diting.dao.CompanyMapper;
import com.diting.dao.RobotMapper;
import com.diting.error.AppErrors;
import com.diting.model.*;
import com.diting.model.options.CompanyOptions;
import com.diting.model.result.Results;
import com.diting.service.ChatLogCountMongoService;
import com.diting.service.CompanyService;
import com.diting.service.FansService;
import com.diting.service.WalletService;
import com.diting.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.diting.util.Utils.isEmpty;

@SuppressWarnings("ALL")
@Service("companyService")
@Transactional
public class CompanyServiceImpl implements CompanyService {


    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RobotMapper robotMapper;

    @Autowired
    private ChatLogCountMongoService chatLogCountMongoService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private ChatStatisticsMapper chatStatisticsMapper;

    @Autowired
    private FansService fansService;

    @Override
    public Company create(Company company) {
        companyMapper.create(company);
        return company;
    }

    @Override
    public Company save(Company company) {

        if (companyMapper.checkNameExists(company.getId(), company.getName()))
            throw AppErrors.INSTANCE.common("所有者名称已存在！").exception();

        if (company.getId() == null) {
            companyMapper.create(company);

            Account account = accountMapper.get(Universe.current().getUserId());
            if (account == null)
                throw AppErrors.INSTANCE.common("该登录账户不存在！").exception();
            if (!isEmpty(company.getHeadPortrait())){
               account.setHeadImgUrl(company.getHeadPortrait());
            }else if (!isEmpty(company.getHeadImgUrl())){
               account.setHeadImgUrl(company.getHeadImgUrl());
            }
            account.setCompanyId(company.getId());
            accountMapper.update(account);
        } else {
            Account account = accountMapper.get(Universe.current().getUserId());
            if (account == null)
                throw AppErrors.INSTANCE.common("该登录账户不存在！").exception();
            if (!isEmpty(company.getHeadPortrait())){
                account.setHeadImgUrl(company.getHeadPortrait());
            }else {
                account.setHeadImgUrl(company.getHeadImgUrl());
            }
            accountMapper.update(account);
            companyMapper.update(company);
        }
        return company;
    }

    @Override
    public Company update(Company company) {
        companyMapper.update(company);
        return company;
    }

    @Override
    public Company get(Integer companyId) {
        Company company=companyMapper.get(companyId);
        Account account=accountMapper.getByCompanyId(companyId);
        if (null!=account) {
            company.setHeadImgUrl(account.getHeadImgUrl());
            company.setHeadPortrait(account.getHeadImgUrl());
        }
        return company;
    }

    @Override
    public Results<Company> searchForPage(CompanyOptions options) {
        Results results = new Results();
        List<Company> companies = new ArrayList<>();
        List<Account> accountList = accountMapper.searchAccountForPage(options);
        for (Account account : accountList) {
            Company company = new Company();
            if (null != account.getCompanyId()) {
                company = companyMapper.get(account.getCompanyId());
                if (isEmpty(company.getCreatedBy())){
                    company.setCreatedBy(account.getUserName());
                }
            }else {
                company.setCreatedBy(account.getUserName());
            }
            company.setSource(account.getSource());
            company.setCreatedTime(account.getCreatedTime());
            company.setChatLogAccount(null==account.getChatStatistics()?0:account.getChatStatistics().getAllCount());
            company.setChatLogYesterdayAccount(null==account.getChatStatistics()?0:account.getChatStatistics().getYesterdayCount());
            company.setAccuracyRate(null==account.getChatStatistics()?"0.00%":account.getChatStatistics().getAccuracyRate());
            company.setResidualFrequency(walletService.getWalletByUserId(account.getId()).intValue());
            companies.add(company);
        }
        if (null==accountList||accountList.size()==0){
            companies=companyMapper.searchForPage(options);
            for (Company company : companies) {
                Account account = accountMapper.getByCompanyId(company.getId());
                Integer chatLogAccountAll = chatLogCountMongoService.getChatLogCountByUsername(account.getMobile());
                Integer chatLogYesterdayAccount = chatLogCountMongoService.getChatLogYesterdayCountByUsername(account.getMobile());
                Integer chatLogInvalidAccount = chatLogCountMongoService.getChatLogInvalidCountByUsername(account.getMobile());
                company.setChatLogAccount(chatLogAccountAll);
                company.setChatLogYesterdayAccount(chatLogYesterdayAccount);
                company.setSource(account.getSource());
                double accuracyRate = 0;
                if (!chatLogAccountAll.equals(0)) {
                    accuracyRate = (double) chatLogInvalidAccount / chatLogAccountAll;
                }
                company.setAccuracyRate(DateUtil.getPercentage(accuracyRate));
                company.setResidualFrequency(walletService.getWalletByUserId(account.getId()).intValue());
            }
        }
        results.setItems(companies);
        results.setTotal(options.getTotalRecord());
        return results;
    }

    @Override
    public Results<Company> searchForPageBytime(CompanyOptions options) {
        Results results = new Results();
        List<Company> companies = new ArrayList<>();
        List<Account> accountList = accountMapper.searchForPageBytime(options);
        for (Account account:accountList){
            Company company=companyMapper.get(account.getCompanyId());
            if (null==company){
                company=new Company();
                company.setCreatedTime(account.getCreatedTime());
            }
            company.setCreatedBy(account.getUserName());
            company.setSource(account.getSource());
            Integer chatLogAccountAll = chatLogCountMongoService.getChatLogCountByUsername(account.getMobile());
            Integer chatLogYesterdayAccount = chatLogCountMongoService.getChatLogYesterdayCountByUsername(account.getMobile());
            Integer chatLogInvalidAccount = chatLogCountMongoService.getChatLogInvalidCountByUsername(account.getMobile());
            company.setChatLogAccount(chatLogAccountAll);
            company.setChatLogYesterdayAccount(chatLogYesterdayAccount);
            double accuracyRate = 0;
            if (!chatLogAccountAll.equals(0)) {
                accuracyRate = (double) chatLogInvalidAccount / chatLogAccountAll;
            }
            company.setAccuracyRate(DateUtil.getPercentage(accuracyRate));
            company.setResidualFrequency(walletService.getWalletByUserId(account.getId()).intValue());
            companies.add(company);
        }
        results.setItems(companies);
        results.setTotal(options.getTotalRecord());
        return results;
    }


    @Override
    public Results<Company> searchAccountByWhereForPage(CompanyOptions options){
        Results results = new Results();
        List<Company> companies = new ArrayList<>();
        List<Account> accountList = accountMapper.searchAccountForPage(options);
        for (Account account : accountList) {
            Company company = new Company();
            if (null != account.getCompanyId()) {
                company = companyMapper.get(account.getCompanyId());
                if (isEmpty(company.getCreatedBy())){
                    company.setCreatedBy(account.getUserName());
                }
            }else {
                company.setCreatedBy(account.getUserName());
            }
            company.setSource(account.getSource());
            company.setCreatedTime(account.getCreatedTime());
            company.setChatLogAccount(null==account.getChatStatistics()?0:account.getChatStatistics().getAllCount());
            company.setChatLogYesterdayAccount(null==account.getChatStatistics()?0:account.getChatStatistics().getYesterdayCount());
            company.setAccuracyRate(null==account.getChatStatistics()?"0.00%":account.getChatStatistics().getAccuracyRate());
            company.setResidualFrequency(walletService.getWalletByUserId(account.getId()).intValue());
            companies.add(company);
        }
        if (null==accountList||accountList.size()==0){
            companies=companyMapper.searchForPage(options);
            for (Company company : companies) {
                Account account = accountMapper.getByCompanyId(company.getId());
                Integer chatLogAccountAll = chatLogCountMongoService.getChatLogCountByUsername(account.getMobile());
                Integer chatLogYesterdayAccount = chatLogCountMongoService.getChatLogYesterdayCountByUsername(account.getMobile());
                Integer chatLogInvalidAccount = chatLogCountMongoService.getChatLogInvalidCountByUsername(account.getMobile());
                company.setChatLogAccount(chatLogAccountAll);
                company.setChatLogYesterdayAccount(chatLogYesterdayAccount);
                company.setSource(account.getSource());
                double accuracyRate = 0;
                if (!chatLogAccountAll.equals(0)) {
                    accuracyRate = (double) chatLogInvalidAccount / chatLogAccountAll;
                }
                company.setAccuracyRate(DateUtil.getPercentage(accuracyRate));
                company.setResidualFrequency(walletService.getWalletByUserId(account.getId()).intValue());
            }
        }
        results.setItems(companies);
        results.setTotal(options.getTotalRecord());
        return results;
    }

    @Override
    public Results<Company> searchAgentCompanyForPage(CompanyOptions options) {
        Results results = new Results();
        List<Company> companies = new ArrayList<>();
        List<Account> accountList = null;
        if (isEmpty(options.getInvitationCode())) {
            accountList = new ArrayList<>();
        } else {
            accountList = accountMapper.searchAgentForPage(options);
        }
        for (Account account : accountList) {
            Company company = null;
            if (null == account.getCompanyId()) {
                company = new Company();
                company.setCreatedBy(account.getUserName());
                company.setCreatedTime(account.getCreatedTime());
            } else {
                company = companyMapper.get(account.getCompanyId());
                Integer chatLogAccountAll = chatLogCountMongoService.getChatLogCountByUsername(account.getMobile());
                Integer chatLogYesterdayAccount = chatLogCountMongoService.getChatLogYesterdayCountByUsername(account.getMobile());
                Integer chatLogInvalidAccount = chatLogCountMongoService.getChatLogInvalidCountByUsername(account.getMobile());
                company.setChatLogAccount(chatLogAccountAll);
                company.setChatLogYesterdayAccount(chatLogYesterdayAccount);
                double accuracyRate = 0;
                if (!chatLogAccountAll.equals(0)) {
                    accuracyRate = (double) chatLogInvalidAccount / chatLogAccountAll;
                }
                company.setAccuracyRate(DateUtil.getPercentage(accuracyRate));
                company.setResidualFrequency(walletService.getWalletByUserId(account.getId()).intValue());
            }
            companies.add(company);
        }
        results.setItems(companies);
        results.setTotal(options.getTotalRecord());
        return results;
    }

    @Override
    public List<Company> searchForKeyWord(String keyWord) {
        List<Company> companies = companyMapper.searchForKeyWord(keyWord);
        int i = 0;
        for (Company company : companies) {
            List<Robot> robots = robotMapper.getByNameOrUniqueId(null, company.getId());
            ChatStatistics chatStatistics=chatStatisticsMapper.findByCompanyId(company.getId());
            if (null!=chatStatistics){
                company.setRobotVal(String.valueOf(chatStatistics.getRobotValue()));
                company.setRobotValue(chatStatistics.getRobotValue());
            }
            Account account = accountMapper.getByCompanyId(company.getId());
            if (null!=account&&null!=Universe.current().getUserId()){
                Fans fans=fansService.searchFansByUserId(account.getId(),Universe.current().getUserId());
                if (null!=fans){
                    company.setAttentionState("true");
                }else {
                    company.setAttentionState("false");
                }
            }else {
                company.setAttentionState("false");
            }
            if (robots != null && robots.size() > 0) {
                company.setUniqueId(robots.get(0).getUniqueId());
                company.setRobotName(robots.get(0).getName());
                company.setProfile(robots.get(0).getProfile());
                company.setWelcome(robots.get(0).getWelcomes());
                company.setUsername(null == account ? null : account.getUserName());
                company.setUserId(null == account ? null : account.getId());
                company.setHeadImgUrl(null == account ? null : account.getHeadImgUrl());
                company.setCompanyName(company.getName());
                if (isEmpty(company.getName()) && !isEmpty(robots.get(0).getName())) {
                    company.setName(robots.get(0).getName());
                }
            }
            company.setNum(0);
        }
        if (companies == null || companies.size() == 0) {
            List<Robot> robotList = robotMapper.getByNameOrUniqueId(keyWord, null);
            for (Robot robot : robotList) {
                Account account = accountMapper.get(robot.getAccountId());
                Company company = new Company();
                if (null != account.getCompanyId()) {
                    company = companyMapper.get(account.getCompanyId());
                    ChatStatistics chatStatistics=chatStatisticsMapper.findByCompanyId(account.getCompanyId());
                    if (null!=chatStatistics){
                        company.setRobotVal(String.valueOf(chatStatistics.getRobotValue()));
                        company.setRobotValue(chatStatistics.getRobotValue());
                    }
                }
                if (null!=account&&null!=Universe.current().getUserId()){
                    Fans fans=fansService.searchFansByUserId(account.getId(),Universe.current().getUserId());
                    if (null!=fans){
                        company.setAttentionState("true");
                    }else {
                        company.setAttentionState("false");
                    }
                }else {
                    company.setAttentionState("false");
                }
                company.setUniqueId(robot.getUniqueId());
                company.setName(robot.getName());
                company.setRobotName(robot.getName());
                company.setProfile(robot.getProfile());
                company.setWelcome(robot.getWelcomes());
                company.setUsername(null == account ? null : account.getUserName());
                company.setUserId(null == account ? null : account.getId());
                company.setHeadImgUrl(null == account ? null : account.getHeadImgUrl());
                company.setCompanyName(company.getName());
                company.setNum(0);
                companies.add(company);
            }
        }
        return companies;
    }

    @Override
    public Boolean checkCompanyNameExists(String companyName) {
        return companyMapper.checkCompanyNameExists(companyName);
    }
}
