/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 SilkCloud and/or its affiliates. All rights reserved.
 */
package com.diting.core;


import com.diting.error.AppErrors;
import com.diting.util.SysConfig;
import com.diting.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import java.io.IOException;


/**
 * SecurityFilter.
 */
public class SecurityFilter implements ContainerRequestFilter {
    private static final String APP_KEY_HEADER = "X-APP-KEY";
    protected static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);

    @Context
    private HttpServletRequest servletRequest;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String remoteAddr = servletRequest.getRemoteAddr();

        // set ip
        Universe.current().setIp(remoteAddr);

        // check whether call from ip white-list
        if (!Utils.isEmpty(remoteAddr)) {
            LOGGER.debug("Request IP: " + servletRequest.getRemoteAddr());

            if (SysConfig.getIpWhiteList().contains(remoteAddr + "#")) return;

        }

        // check whether call with app key from non ip white-list
        if (!SysConfig.getAppKey().equals(containerRequestContext.getHeaderString(APP_KEY_HEADER))) {
            throw AppErrors.INSTANCE.accessDenied().exception();
        }
    }
}
