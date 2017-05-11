package com.diting.service.impl;

import com.diting.dao.InternalUserMapper;
import com.diting.error.AppErrors;
import com.diting.model.InternalUser;
import com.diting.model.options.InternalUserOptions;
import com.diting.model.result.Results;
import com.diting.service.InternalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */

@SuppressWarnings("ALL")
@Service("internalUserService")
public class InternalUserServiceImpl implements InternalUserService {

    @Autowired
    private InternalUserMapper userMapper;

    @Override
    public InternalUser create(InternalUser internalUser) {
        if (checkUser(internalUser.getUsername())){
            throw AppErrors.INSTANCE.usernameAlreadyExists(internalUser.getUsername()).exception();
        }
        internalUser.setRole("administrator");
        userMapper.create(internalUser);
        return internalUser;
    }

    @Override
    public InternalUser login(InternalUser internalUser) {
        // check username not null
        if (internalUser.getUsername() == null)
            throw AppErrors.INSTANCE.missingField("username").exception();

        // check password not null
        if (internalUser.getPassword() == null)
            throw AppErrors.INSTANCE.missingField("password").exception();
        InternalUser internalUser1=userMapper.checkUserNameLogin(internalUser);
        return null;
    }

    @Override
    public InternalUser update(InternalUser internalUser) {
        userMapper.update(internalUser);
        return internalUser;
    }

    @Override
    public Results<InternalUser> searchForPage(InternalUserOptions internalUserOptions) {
        Results results = new Results();
        List<InternalUser> internalUsers = userMapper.searchForPage(internalUserOptions);
        results.setItems(internalUsers);
        results.setTotal(internalUserOptions.getTotalRecord());
        return results;
    }

    public boolean checkUser(String username){
        return userMapper.checkInternalUserExists(username);
    }
}
