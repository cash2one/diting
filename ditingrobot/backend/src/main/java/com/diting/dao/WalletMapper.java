/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.dao;

import com.diting.model.Wallet;
import com.diting.model.options.WalletSearchOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * WalletMapper.
 */
public interface WalletMapper{

    void create(Wallet wallet);

    void update(Wallet wallet);

    Wallet get(@Param("walletId") Integer walletId);

    Wallet searchWalletByUserId(@Param("userId") Integer userId);

    List<Wallet> search(WalletSearchOptions options);

    List<Wallet> getWalletByLegerType(WalletSearchOptions options);

    boolean checkWalletExists(@Param("userId") Integer userId, @Param("userType") String userType, @Param("type") String type);
}
