/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.model.enums;

/**
 * WalletTransactionType.
 */
public enum WalletTransactionType {
    CREDIT,
    DEBIT,
    REFUND,     // refund debit
    REVOKE,     // revoke credit
    TRANSFER_IN,
    TRANSFER_OUT
}
