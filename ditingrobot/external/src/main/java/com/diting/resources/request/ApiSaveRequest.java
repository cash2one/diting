/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.resources.request;

import com.diting.model.ApiPersonalStore;
import com.diting.model.ApiStore;
import com.diting.model.Parameter;

import java.util.List;

/**
 * ApiSaveRequest.
 */
public class ApiSaveRequest {
    private ApiPersonalStore apiPersonalStore;
    private ApiStore apiStore;
    private List<Parameter> parameters;

    public ApiPersonalStore getApiPersonalStore() {
        return apiPersonalStore;
    }

    public void setApiPersonalStore(ApiPersonalStore apiPersonalStore) {
        this.apiPersonalStore = apiPersonalStore;
    }

    public ApiStore getApiStore() {
        return apiStore;
    }

    public void setApiStore(ApiStore apiStore) {
        this.apiStore = apiStore;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
