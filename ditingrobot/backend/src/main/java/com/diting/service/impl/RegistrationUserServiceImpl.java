package com.diting.service.impl;

import com.diting.core.Universe;
import com.diting.dao.AccountMapper;
import com.diting.dao.RegistrationUserMapper;
import com.diting.dao.VoteLogMapper;
import com.diting.error.AppErrors;
import com.diting.model.Account;
import com.diting.model.RegistrationUser;
import com.diting.model.VoteLog;
import com.diting.service.RegistrationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
@SuppressWarnings("ALL")
@Service("registrationUserService")
public class RegistrationUserServiceImpl implements RegistrationUserService {

    @Autowired
    private RegistrationUserMapper registrationUserMapper;

    @Autowired
    private VoteLogMapper voteLogMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public RegistrationUser create(RegistrationUser registrationUser) {
        registrationUserMapper.create(registrationUser);
        return registrationUser;
    }

    @Override
    public List<RegistrationUser> search() {
        return registrationUserMapper.search();
    }

    @Override
    public List<RegistrationUser> searchForVote() {
        return registrationUserMapper.searchForVote();
    }

    @Override
    public Boolean checkMobileExists(String mobile) {
        return registrationUserMapper.checkMobileExists(mobile);
    }

    @Override
    public void vote(String mobile) {
        Account account = accountMapper.get(Universe.current().getUserId());
        RegistrationUser registrationUser = registrationUserMapper.getByMobile(mobile);

        Integer voteCount = voteLogMapper.getVoteCount(account.getMobile());

        if (registrationUserMapper.checkMobileExists(account.getMobile())) {
            if (voteLogMapper.checkVoteExists(account.getMobile(), mobile))
                throw AppErrors.INSTANCE.common("只能给每个人投1票！").exception();

            if (voteCount >= 5)
                throw AppErrors.INSTANCE.common("活动参加人员，最多投5票！").exception();
            registrationUser.setAllowCount(registrationUser.getAllowCount() + 1);
        } else {
            if (voteCount > 0)
                throw AppErrors.INSTANCE.common("非活动参加人员，只能投1票！").exception();
            registrationUser.setNotCount(registrationUser.getNotCount() + 1);
        }

        Float score = Float.valueOf(String.valueOf(registrationUser.getAllowCount() * 80 / 100.00 + registrationUser.getNotCount() * 20 / 100.00));
        registrationUser.setSortScore(score);

        registrationUserMapper.update(registrationUser);

        voteLogMapper.create(new VoteLog(account.getMobile(), mobile));

    }
}
