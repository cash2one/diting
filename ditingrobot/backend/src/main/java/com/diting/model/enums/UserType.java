package com.diting.model.enums;

import com.diting.core.Identifiable;
import com.diting.util.Utils;

/**
 * UserType.
 */
public enum UserType implements Identifiable<Integer> {
    DITING_USER(1),
    EMPLOYEE_USER(2),
    SYSTEM(3),
    ANONYMOUS_USER(4),
    AGENT(5),;

    private Integer id;

    UserType(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public static Integer userTypeHandler(String element) {
        if (Utils.equals(element, UserType.DITING_USER.toString())) {
            return UserType.DITING_USER.getId();
        } else if (Utils.equals(element,  UserType.EMPLOYEE_USER.toString())) {
            return UserType.EMPLOYEE_USER.getId();
        } else if (Utils.equals(element,  UserType.SYSTEM.toString())) {
            return UserType.SYSTEM.getId();
        }
        return UserType.ANONYMOUS_USER.getId();
    }
}