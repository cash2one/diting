package com.diting.error;


import com.diting.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Error.
 */
public class Error {
    public Error() {
    }

    public Error(String message, String code, List<ErrorDetail> details) {
        this.message = message;
        this.code = code;
        this.details = details;
    }

    public Error(String message, String code, String field, String reason) {
        this.message = message;
        this.code = code;
        this.details = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setField(field);
        errorDetail.setReason(reason);
        this.details.add(errorDetail);
    }

    private String message;

    private String code;

    private List<ErrorDetail> details;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ErrorDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ErrorDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        try {
            return Utils.toJSON(this);
        } catch (Exception ex) {
            return "Error: " + message + " code: " + code;
        }
    }
}
