/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.cache;

/**
 * Cache.
 */
public interface Cache {
    void put(String key, Object value);

    <T> T get(String key);

    void put(String key, Object value, Integer expire);

    long getTTL(String key);

    long incr(String key);

    long del(String key);
}
