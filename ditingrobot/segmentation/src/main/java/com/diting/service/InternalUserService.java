package com.diting.service;

import com.diting.model.InternalUser;
import com.diting.model.options.InternalUserOptions;
import com.diting.model.result.Results;

/**
 * Created by Administrator on 2016/12/28.
 */
public interface InternalUserService {

    InternalUser create(InternalUser internalUser);

    InternalUser login(InternalUser internalUser);

    InternalUser update(InternalUser internalUser);

    Results<InternalUser> searchForPage(InternalUserOptions internalUserOptions);
}
