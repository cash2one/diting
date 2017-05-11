/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.bootstrap;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * DiTingConfiguration.
 */
@ImportResource({"classpath*:META-INF/spring/context-entry.xml"})
@ComponentScan(basePackages = {"com"})
public class DiTingConfiguration extends Configuration {
    private String environment;

    @JsonProperty
    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
