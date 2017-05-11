package com.diting.resources.request;

import java.math.BigDecimal;
import java.util.UUID;

public class CreditWalletRequest {
    private Integer walletId;
    private String lotType;
    private String reason;
    private String event;
    private BigDecimal amount;
   
    private String trackingUuid; //建一个BaseRequest放进去， trackingUuid主要用来防止API重复call，同时可以作为bug研究的线索

    // for transfer
    private Integer targetWalletId;

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}