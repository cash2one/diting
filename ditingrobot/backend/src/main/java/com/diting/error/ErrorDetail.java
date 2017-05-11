package com.diting.error;

/**
 * ErrorDetail.
 */
public class ErrorDetail {
    public ErrorDetail() {
    }

    public ErrorDetail(String field, String reason) {
        this.field = field;
        this.reason = reason;
    }

    private String field;
    private String reason;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
