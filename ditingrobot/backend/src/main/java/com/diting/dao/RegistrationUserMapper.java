package com.diting.dao;

import com.diting.model.RegistrationUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
public interface RegistrationUserMapper {

    void update(RegistrationUser registrationUser);

    void create(RegistrationUser registrationUser);

    List<RegistrationUser> search();

    List<RegistrationUser> searchForVote();

    boolean checkMobileExists(@Param("mobile") String mobile);

    RegistrationUser getByMobile(@Param("mobile") String mobile);

}
