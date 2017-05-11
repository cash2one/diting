package com.diting.service.impl;

import com.diting.dao.AccountMapper;
import com.diting.dao.RecruitMapper;
import com.diting.error.AppErrors;
import com.diting.model.Account;
import com.diting.model.Recruit;
import com.diting.service.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/1/4.
 */
@SuppressWarnings("ALL")
@Service
public class RecruitServiceImpl implements RecruitService {

    @Autowired
    private RecruitMapper recruitMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Recruit create(Recruit recruit) {
        if (recruit.getUsername()==null)
            throw AppErrors.INSTANCE.common("账号异常").exception();
        Account account=accountMapper.getByMobile(recruit.getUsername());
        recruit.setAccountId(account.getId());
        recruit.setCompanyId(account.getCompanyId());
        recruitMapper.create(recruit);
        return recruit;
    }

    @Override
    public Recruit update(Recruit recruit) {
        recruitMapper.update(recruit);
        return recruit;
    }

    @Override
    public Boolean delete(Integer id) {
        return recruitMapper.delete(id);
    }
}
