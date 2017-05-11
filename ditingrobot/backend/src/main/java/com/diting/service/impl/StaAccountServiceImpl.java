package com.diting.service.impl;

import com.diting.dao.StaAccountMapper;
import com.diting.model.StaAccount;
import com.diting.model.options.PageableOptions;
import com.diting.model.result.Results;
import com.diting.service.StaAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Service("staAccountService")
@Transactional
public class StaAccountServiceImpl implements StaAccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaAccountServiceImpl.class);

    private static final String PART_LEGER_TYPE_SUFFIX = "_PART";

    @Autowired
    private StaAccountMapper accountMapper;

    @Override
    public StaAccount create(StaAccount staAccount) {
        accountMapper.create(staAccount);
        return staAccount;
    }

    @Override
    public StaAccount update(StaAccount staAccount) {
        accountMapper.update(staAccount);
        return staAccount;
    }

    @Override
    public Results<StaAccount> searchForPage(PageableOptions pageableOptions) {
        Results results = new Results();
        List<StaAccount> accounts = accountMapper.searchForPage(pageableOptions);
        results.setItems(accounts);
        Integer allAccountCount = 0;
        Integer allValidCount = 0;
        Integer allInvalidCount = 0;
        Integer allQuestionCount = 0;
        Integer allKnowledgeCount = 0;
        List<StaAccount> staAccountList = accountMapper.findAll();
        for (StaAccount staAccount : staAccountList) {
            allAccountCount += staAccount.getDayAccountCount();
            allValidCount += staAccount.getDayValidCount();
            allInvalidCount += staAccount.getDayInvalidCount();
            allQuestionCount += staAccount.getDayQuestionCount();
            allKnowledgeCount += staAccount.getDayKnowledgeCount();
            for (StaAccount staAccount1 : accounts) {
                if (staAccount.getId().equals(staAccount1.getId())) {
                    staAccount1.setAllAccountCount(allAccountCount);
                    staAccount1.setAllValidCount(allValidCount);
                    staAccount1.setAllInvalidCount(allInvalidCount);
                    staAccount1.setAllQuestionCount(allQuestionCount);
                    staAccount1.setAllKnowledgeCount(allKnowledgeCount);
                    break;
                }
            }

        }
        results.setTotal(pageableOptions.getTotalRecord());
        return results;
    }

    @Override
    public List<StaAccount> findAllStaAccount() {
        Integer allAccountCount = 0;
        Integer allValidCount = 0;
        Integer allInvalidCount = 0;
        Integer allQuestionCount = 0;
        Integer allKnowledgeCount = 0;
        List<StaAccount> staAccountList = accountMapper.findAll();
        for (StaAccount staAccount : staAccountList) {
            allAccountCount += staAccount.getDayAccountCount();
            allValidCount += staAccount.getDayValidCount();
            allInvalidCount += staAccount.getDayInvalidCount();
            allQuestionCount += staAccount.getDayQuestionCount();
            allKnowledgeCount += staAccount.getDayKnowledgeCount();
            staAccount.setAllAccountCount(allAccountCount);
            staAccount.setAllValidCount(allValidCount);
            staAccount.setAllInvalidCount(allInvalidCount);
            staAccount.setAllQuestionCount(allQuestionCount);
            staAccount.setAllKnowledgeCount(allKnowledgeCount);
        }
        return staAccountList;
    }

}
