/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.model;


import java.math.BigDecimal;

/**
 * WalletTransaction.
 */
public class WalletTransaction extends BaseModel {
    private Integer walletId;
    private String type;
    private String trackingUuid;
    private String event;
    private String reason;
    private BigDecimal amount;
    private BigDecimal originalBalance;
    private BigDecimal currentBalance;

    // for transfer
    private Integer targetWalletId;

    public WalletTransaction() {

    }

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getOriginalBalance() {
        return originalBalance;
    }

    public void setOriginalBalance(BigDecimal originalBalance) {
        this.originalBalance = originalBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
