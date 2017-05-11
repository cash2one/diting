/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2014 RayBow and/or its affiliates. All rights reserved.
 */
package com.diting.cache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static com.diting.util.Utils.*;

/**
 * RedisCacheImpl.
 */
public class RedisImpl extends CacheSupport implements InitializingBean {
    @Value("${cache.redis.enabled}")
    private boolean enabled;

    @Value("${cache.redis.host}")
    private String redisHost;

    @Value("${cache.redis.port}")
    private Integer redisPort;

    @Value("${cache.redis.password}")
    private String password;

    @Value("${cache.redis.minIdle}")
    private String minIdle;

    @Value("${cache.redis.maxIdle}")
    private Integer maxIdle;

    @Value("${cache.redis.maxTotal}")
    private Integer maxTotal;

    @Value("${cache.redis.maxWait}")
    private Integer maxWait;

    @Value("${cache.redis.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${cache.redis.betweenEvicationRuns}")
    private Integer betweenEvicationRuns;

    private JedisPool pool;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (!enabled) {
            LOGGER.warn("Redis cache is disabled.");
            return;
        }
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(maxTotal);
            config.setMaxIdle(maxIdle);
            config.setMaxWaitMillis(maxWait * 60 * 1000);
            config.setTimeBetweenEvictionRunsMillis(betweenEvicationRuns * 60 * 1000);
            config.setTestWhileIdle(testWhileIdle);


            pool = new JedisPool(config, redisHost, redisPort);

        } catch (Exception e) {
            LOGGER.warn("Error occurred during initializing redis cache client.");
        }
    }

    public void put(String key, Object value) {
        if (!enabled) {
            LOGGER.warn("Redis cache is disabled.");
            return;
        }
        Jedis client = null;
        boolean success = true;
        try {
            client = pool.getResource();
            client.auth(password);

            client.set(key.getBytes(), serialize(value));
        } catch (Exception e) {
            success = false;
            if (client != null) {
                pool.returnBrokenResource(client);
            }
            throw new RuntimeException(e);
        } finally {
            if (client != null && success) {
                pool.returnResource(client);
            }
        }
    }

    public <T> T get(String key) {
        if (!enabled) {
            LOGGER.warn("Redis cache is disabled.");
            return null;
        }

        Jedis client = null;
        boolean success = true;
        byte[] bytes = null;
        try {
            client = pool.getResource();
            client.auth(password);

            bytes = client.get(key.getBytes());
        } catch (Exception e) {
            success = false;
            if (client != null) {
                pool.returnBrokenResource(client);
            }
            throw new RuntimeException(e);
        } finally {
            if (client != null && success) {
                pool.returnResource(client);
            }
        }
        if (bytes != null)
            return deserialize(bytes);
        else
            return null;
    }

    public String strGet(String key) {
        if (!enabled) {
            LOGGER.warn("Redis cache is disabled.");
            return null;
        }

        Jedis client = null;
        boolean success = true;
        byte[] bytes = null;
        try {
            client = pool.getResource();
            client.auth(password);

            bytes = client.get(key.getBytes());
        } catch (Exception e) {
            success = false;
            if (client != null) {
                pool.returnBrokenResource(client);
            }
            throw new RuntimeException(e);
        } finally {
            if (client != null && success) {
                pool.returnResource(client);
            }
        }
        if (bytes != null)
            return toStr(bytes);
        else
            return null;
    }

    /**
     * @param key
     * @param value
     * @param expireAt
     */
    public void put(String key, Object value, Long expireAt) {
        if (!enabled) {
            LOGGER.warn("Redis cache is disabled.");
            return;
        }

        Jedis client = null;
        boolean success = true;
        try {
            client = pool.getResource();
            client.auth(password);

            client.set(key.getBytes(), serialize(value.toString()));
            client.expireAt(key.getBytes(), expireAt / 1000);//system.currentTimeMillis/1000=unixTime
        } catch (Exception e) {
            success = false;
            if (client != null) {
                pool.returnBrokenResource(client);
            }
            throw new RuntimeException(e);
        } finally {
            if (client != null && success) {
                pool.returnResource(client);
            }
        }
    }

    /**
     * @param key
     * @param value
     * @param expireAt
     */
    public void put(String key, String value, Long expireAt) {
        if (!enabled) {
            LOGGER.warn("Redis cache is disabled.");
            return;
        }

        Jedis client = null;
        boolean success = true;
        try {
            client = pool.getResource();
            client.auth(password);

            client.set(key.getBytes(), toBytes(value));
            client.expireAt(key.getBytes(), expireAt / 1000);//system.currentTimeMillis/1000=unixTime
        } catch (Exception e) {
            success = false;
            if (client != null) {
                pool.returnBrokenResource(client);
            }
            throw new RuntimeException(e);
        } finally {
            if (client != null && success) {
                pool.returnResource(client);
            }
        }
    }

    public boolean exist(String key) {
        if (!enabled) {
            LOGGER.warn("Redis cache is disabled.");
            throw new IllegalStateException("Redis cache is disabled.");
        }
        Jedis client = null;
        boolean success = true;
        boolean isExist = false;
        try {
            client = pool.getResource();
            client.auth(password);

            isExist = client.exists(key.getBytes());
        } catch (Exception e) {
            success = false;
            if (client != null) {
                pool.returnBrokenResource(client);
            }
            throw new RuntimeException(e);
        } finally {
            if (client != null && success) {
                pool.returnResource(client);
            }
        }
        return isExist;
    }

    /**
     * @param expire 失效时间秒
     */
    public void put(String key, Object value, Integer expire) {
        if (!enabled) {
            LOGGER.warn("Redis cache is disabled.");
            return;
        }

        Jedis client = null;
        boolean success = true;
        try {
            client = pool.getResource();
            client.auth(password);
            client.set(key.getBytes(), serialize(value));
            client.expire(key.getBytes(), expire);
        } catch (Exception e) {
            success = false;
            if (client != null) {
                pool.returnBrokenResource(client);
            }
            throw new RuntimeException(e);
        } finally {
            if (client != null && success) {
                pool.returnResource(client);
            }
        }
    }

    public long getTTL(String key) {
        if (!enabled) {
            LOGGER.warn("Redis cache is disabled.");
            return -1;
        }

        Jedis client = null;
        boolean success = true;
        try {
            client = pool.getResource();
            client.auth(password);
            return client.ttl(key.getBytes());
        } catch (Exception e) {
            success = false;
            if (client != null) {
                pool.returnBrokenResource(client);
            }
            throw new RuntimeException(e);
        } finally {
            if (client != null && success) {
                pool.returnResource(client);
            }
        }
    }

    @Override
    public long incr(String key) {
        if (!enabled) {
            LOGGER.warn("Redis cache is disabled.");
            return -1;
        }
        Jedis client = null;
        boolean success = true;
        try {
            client = pool.getResource();
            client.auth(password);
            return client.incr(key.getBytes());
        } catch (Exception e) {
            success = false;
            if (client != null) {
                pool.returnResourceObject(client);
            }
            throw new RuntimeException(e);
        } finally {
            if (client != null && success) {
                pool.returnResourceObject(client);
            }
        }
    }

    public long del(String key) {
        if (!enabled) {
            LOGGER.warn("Redis cache is disabled.");
            return -1;
        }
        Jedis client = null;
        boolean success = true;
        try {
            client = pool.getResource();
            client.auth(password);
            return client.del(key.getBytes());
        } catch (Exception e) {
            success = false;
            if (client != null) {
                pool.returnResourceObject(client);
            }
            throw new RuntimeException(e);
        } finally {
            if (client != null && success) {
                pool.returnResourceObject(client);
            }
        }
    }

    public boolean setnx(String key, String value) {
        return setnx(key, value, null);
    }

    public boolean setnx(String key, String value, Integer expire) {
        if (!enabled) {
            LOGGER.warn("Redis cache is disabled.");
            return false;
        }

        Jedis client = null;
        boolean success = true;
        try {
            client = pool.getResource();
            client.auth(password);
            long result = client.setnx(key.getBytes(), serialize(value));
            if(result == 0){
                return false;
            }
            if (expire != null) {
                client.expire(key.getBytes(), expire);
            }
            return true;
        } catch (Exception e) {
            success = false;
            if (client != null) {
                pool.returnResourceObject(client);
            }
            throw new RuntimeException(e);
        } finally {
            if (client != null && success) {
                pool.returnResourceObject(client);
            }
        }
    }

    private void checkClientState(Jedis client) {
        if (client == null) {
            throw new IllegalStateException("Redis client has not been initialized properly");
        }
    }
}
