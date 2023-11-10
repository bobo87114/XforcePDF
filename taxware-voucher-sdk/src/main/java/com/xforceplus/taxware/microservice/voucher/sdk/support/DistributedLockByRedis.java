package com.xforceplus.taxware.microservice.voucher.sdk.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 基于Redis实现的分布式锁
 *
 * @author Bobo
 * @date 2017/7/11
 */
public class DistributedLockByRedis {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JedisPool jedisPool;
    private String lockKey;
    private static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 100;

    private static final int DEFAULT_SLEEP_RESOLUTION_MILLIS = 300;
    /**
     * 锁超时时间，防止线程在入锁以后，无限的执行等待
     */
    private int expireMsecs = 60 * 1000;
    /**
     * 锁等待时间，防止线程饥饿
     */
    private int timeoutMsecs = 10 * 1000;

    private volatile boolean locked = false;

    public DistributedLockByRedis(JedisPool jedisPool, String lockKey) {
        this.jedisPool = jedisPool;
        this.lockKey = lockKey + "_lock";
    }

    public DistributedLockByRedis(JedisPool jedisPool, String lockKey, int timeoutMsecs) {
        this(jedisPool, lockKey);
        this.timeoutMsecs = timeoutMsecs;
    }

    public DistributedLockByRedis(JedisPool jedisPool, String lockKey, int timeoutMsecs, int expireMsecs) {
        this(jedisPool, lockKey, timeoutMsecs);
        this.expireMsecs = expireMsecs;
    }

    /**
     * 获得 lock.
     * 实现思路: 主要是使用了redis 的setnx命令,缓存了锁.
     * reids缓存的key是锁的key,所有的共享, value是锁的到期时间(注意:这里把过期时间放在value了,没有时间上设置其超时时间)
     * 执行过程:
     * 1.通过setnx尝试设置某个key的值,成功(当前没有这个锁)则返回,成功获得锁
     * 2.锁已经存在则获取锁的到期时间,和当前时间比较,超时的话,则设置新的值
     *
     * @return true if lock is acquired, false acquire timeout
     * @throws InterruptedException in case of thread interruption
     */
    public synchronized boolean lock() {
        int timeout = timeoutMsecs;
        while (timeout >= 0) {
            long expires = System.currentTimeMillis() + expireMsecs + 1;
            String expiresStr = String.valueOf(expires); //锁到期时间
            if (setNX(lockKey, expiresStr)) {
                // lock acquired
                locked = true;
                return true;
            }
            //已加锁
            String currentValueStr = this.get(lockKey); //redis里的时间
            //判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
            if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                //获取上一个锁到期时间，并设置现在的锁到期时间，
                //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
                String oldValueStr = this.getSet(lockKey, expiresStr);
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    //防止误删（覆盖，因为key是相同的）了他人的锁——这里达不到效果，这里值会被覆盖，但是因为什么相差了很少的时间，所以可以接受
                    //[分布式的情况下]:如果这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
                    // lock acquired
                    locked = true;
                    return true;
                }
            }
            timeout -= DEFAULT_ACQUIRY_RESOLUTION_MILLIS;
            try {
                Thread.sleep(DEFAULT_SLEEP_RESOLUTION_MILLIS);
            } catch (InterruptedException e) {
                logger.error("do loop get sleep exception, key : {}", lockKey, e);
            }
        }
        return false;
    }

    public synchronized void unlock() {
        if (locked) {
            Jedis jedis = null;
            try {
                jedis = this.jedisPool.getResource();
                jedis.del(lockKey);
                locked = false;
            } catch (Exception e) {
                logger.error("unlock redis error, key : {}", lockKey, e);
                if (jedis != null) {
                    this.jedisPool.returnBrokenResource(jedis);
                }
            } finally {
                if (jedis != null) {
                    this.jedisPool.returnBrokenResource(jedis);
                }
            }
        }
    }

    private boolean setNX(final String key, final String value) {
        boolean success = false;
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            Long e = jedis.setnx(key, value);
            success = e.compareTo(1L) == 0 ? true : false;
        } catch (Exception e) {
            logger.error("setNX redis error, key : {}", key, e);
            if (jedis != null) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (jedis != null) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        }
        return success;
    }

    private String get(final String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            logger.error("get redis error, key : {}", key, e);
            if (jedis != null) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (jedis != null) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        }
        return value;
    }

    private String getSet(final String key, final String value) {
        String obj = null;
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            obj = jedis.getSet(key, value);
        } catch (Exception e) {
            logger.error("getSet redis error, key : {}", key, e);
            if (jedis != null) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (jedis != null) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        }
        return obj;
    }

    public void setTimeoutMsecs(int timeoutMsecs) {
        this.timeoutMsecs = timeoutMsecs;
    }
}
