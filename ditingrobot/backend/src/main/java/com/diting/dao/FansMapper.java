/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.dao;

import com.diting.model.Fans;
import com.diting.model.Mail;
import com.diting.model.Mail_phone;
import com.diting.model.options.FansOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface FansMapper {

    void save_fans(Fans fans);

    void del_fans(Fans fans);

    List<Fans> find_fans(Fans fans);

    List<Fans> search_fans();

    List<Fans> search_count_fans(Fans fans);

    List<Fans> search_my_fans(Fans fans);

    List<Fans> search_my_fansForPage(FansOptions options);

    List<Fans> search_fans_top();

    List<Fans> search_fans_count(Fans fans);

    List<Fans> search_fans_flg(Fans fans);

    List<Fans> find_fans_owner(Fans fans);

    Fans search_fans_compy(Fans fans);

    Integer find_fans_count(Fans fans);

    Fans searchFansByUserId(@Param("ownUserId") Integer ownUserId, @Param("othUserId") Integer othUserId);

}
