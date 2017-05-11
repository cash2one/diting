/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.model;


import java.math.BigDecimal;
import java.util.UUID;

/**
 * WalletLotTransaction.
 */
public class WalletLotTransaction extends BaseModel {
    private Integer walletLotId;
    private Integer walletId;
    private Integer transactionId;
    private String transactionType;
    private String lotType;
    private String trackingUuid;
    private BigDecimal amount;

    // for transfer
    private Integer targetWalletId;

    public Integer getWalletLotId() {
        return walletLotId;
    }

    public void setWalletLotId(Integer walletLotId) {
        this.walletLotId = walletLotId;
    }

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTrackingUuid() {
        return trackingUuid;
    }

    public void setTrackingUuid(String trackingUuid) {
        this.trackingUuid = trackingUuid;
    }

    public Integer getTargetWalletId() {
        return targetWalletId;
    }

    public void setTargetWalletId(Integer targetWalletId) {
        this.targetWalletId = targetWalletId;
    }
}
