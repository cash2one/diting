package com.diting.service;

import com.diting.model.RegistrationUser;

import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
public interface RegistrationUserService {
    RegistrationUser create(RegistrationUser registrationUser);

    List<RegistrationUser> search();

    List<RegistrationUser> searchForVote();

    void vote(String mobile);

    Boolean checkMobileExists(String mobile);
}
