package com.diting.model.options;

import com.diting.model.enums.UserType;

/**
 * BaseOptions.
 */
public class BaseOptions {
    private Integer ownerId;
    private UserType ownerType;

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public UserType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(UserType ownerType) {
        this.ownerType = ownerType;
    }
}