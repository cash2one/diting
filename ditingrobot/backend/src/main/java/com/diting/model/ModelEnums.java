/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.model;

import com.diting.model.enums.WalletType;

/**
 * ModelEnums.
 */
public final class ModelEnums {

    //interceptor
    public static final String DB_TYPE_MYSQL = "MYSQL";
    public static final String DB_TYPE_ORACLE = "ORACLE";

    public static final String SQL_STATEMENT_INSERT = "INSERT";
    public static final String SQL_STATEMENT_UPDATE = "UPDATE";

    //wallet
    public static final String DEFAULT_WALLET_CURRENCY = "CNY";
    public static final WalletType DEFAULT_WALLET_TYPE = WalletType.DIBI;
    public static final Integer REGISTER_ENTERPRISE_ACCOUNT_AWARD_MB = 500;

    //verify code type
    public static final String VERIFY_CODE_TYPE_BASIC = "BASIC_";



    private ModelEnums() {
        //private ctor
    }
}
