/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 SilkCloud and/or its affiliates. All rights reserved.
 */

package com.diting.core;

import com.diting.error.AppErrors;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Rest exception mapper.
 */
@Provider
public class RestExceptionMapper implements ExceptionMapper<Exception> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionMapper.class);

    @Override
    public Response toResponse(Exception e) {
        LOGGER.error("Error occurred.", e);

        if (e instanceof WebApplicationException) {    //common service exception
            return ((WebApplicationException) e).getResponse();
        } else if (e instanceof UnrecognizedPropertyException) {    //unnecessary field exception
            return AppErrors.INSTANCE.unnecessaryField(
                    ((UnrecognizedPropertyException) e).getUnrecognizedPropertyName()).exception().getResponse();
        } else if (e instanceof InvalidFormatException) {    //field invalid format exception
            return AppErrors.INSTANCE.fieldNotCorrect(
                    ((InvalidFormatException) e).getPathReference(), e.getMessage()).exception().getResponse();
        } else if (e instanceof JsonParseException) {
            return AppErrors.INSTANCE.invalidJson(e.getMessage()).exception().getResponse();
        } else if (e instanceof JsonMappingException) {
            return AppErrors.INSTANCE.fieldNotCorrect(((JsonMappingException) e).getPathReference(), e.getMessage())
                    .exception().getResponse();
        } else {
            //other exceptions
            return AppErrors.INSTANCE.unCaught(e.getMessage()).exception().getResponse();
        }
    }
}
