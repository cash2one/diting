/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 SilkCloud and/or its affiliates. All rights reserved.
 */
package com.diting.core;


import com.diting.model.enums.UserType;
import sun.misc.BASE64Decoder;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

import static com.diting.util.Utils.isEmpty;
import static com.diting.util.Utils.str;
import static com.diting.util.Utils.str2int;

/**
 * SessionFilter.
 */
public class SessionFilter implements ContainerRequestFilter {
    private static final String USER_NAME_HEADER = "X-USER-NAME";
    private static final String USER_ID_HEADER = "X-USER-ID";
    private static final String USER_TYPE_HEADER = "X-USER-TYPE";
    private static final String REFERRER_HEADER = "X-REFERRER";
    private static final String USER_NAME_BASE64_HEADER = "X-USER-NAME-BASE64";

    private String decode(String str) {
        if (str == null) return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] bytes = decoder.decodeBuffer(str);
            return new String(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        // user name
        String base64UserName = containerRequestContext.getHeaderString(USER_NAME_BASE64_HEADER);

        if (!isEmpty(base64UserName)) {
            Universe.current().setUserName(decode(base64UserName));
        } else {
            Universe.current().setUserName(containerRequestContext.getHeaderString(USER_NAME_HEADER));
        }

        // referrer
        Universe.current().setReferrer(containerRequestContext.getHeaderString(REFERRER_HEADER));

        // user id
        Universe.current().setUserId(str2int(containerRequestContext.getHeaderString(USER_ID_HEADER)));

        //user type
        String userType = containerRequestContext.getHeaderString(USER_TYPE_HEADER);
        if (isEmpty(userType)) {
            Universe.current().setUserType(UserType.userTypeHandler(str(UserType.ANONYMOUS_USER)));
        } else {
            Universe.current().setUserType(UserType.userTypeHandler(userType));
        }

    }
}
