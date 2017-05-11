package com.diting.dao;

import com.diting.model.InternalUser;
import com.diting.model.options.InternalUserOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liufei on 2016/12/28.
 */
public interface InternalUserMapper {
    void create(InternalUser internalUser);
    boolean checkInternalUserExists(@Param("username") String username);
    InternalUser checkUserNameLogin(InternalUser internalUser);
    void update(InternalUser internalUser);
    List<InternalUser> searchForPage(InternalUserOptions internalUserOptions);
}
