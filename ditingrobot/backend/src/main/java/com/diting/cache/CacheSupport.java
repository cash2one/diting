/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CacheSupport.
 */
public abstract class CacheSupport implements Cache {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public void put(String key, Object value) {
        throw new RuntimeException("Not implemented");
    }

    public <T> T get(String key) {
        throw new RuntimeException("Not implemented");
    }

    public void put(String key,Object value,Integer expire){
        throw new RuntimeException("Not implemented");
    }

    public long getTTL(String key){
        throw new RuntimeException("Not implemented");
    }

    public long incr(String key) {
        throw new RuntimeException("Not implemented");
    }

    public long del(String key) {
        throw new RuntimeException("Not implemented");
    }
}
