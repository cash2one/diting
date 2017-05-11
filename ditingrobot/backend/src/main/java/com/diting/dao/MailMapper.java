/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.dao;

import com.diting.model.Mail;
import com.diting.model.Mail_phone;
import com.diting.model.Wallet;
import com.diting.model.options.MailOptions;
import com.diting.model.options.WalletSearchOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * WalletMapper.
 */
public interface MailMapper {



    List<Mail> search();
    public void save(Mail mail);

    List<Mail> search_phoneForPage(MailOptions options);
    public void save_phone(Mail_phone mail);
    List<Mail> find_phone(Mail_phone mail);

}
