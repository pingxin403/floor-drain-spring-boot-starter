package com.github.hanyunpeng0521.floordrain.cache;

import com.github.hanyunpeng0521.floordrain.property.FloorDrainProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * 缓存实现--Map
 *
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain.cache
 * hyp create at 20-3-24
 **/
public class FloorDrainMapCache implements Cache {

    //存储map
    private static final Map<String, CacheObject> STORE = new ConcurrentHashMap<>();
    //5分钟清理一次过期缓存
    private static final int DEFAULT_CLEAR_CACHE_DELAY = 5;
    private static final TimeUnit DEFAULT_CLEAR_CACHE_UNIT = TimeUnit.MINUTES;

    //并发控制,读写+乐观锁，注意与ReentrantReadWriteLock的不同
    private final StampedLock lock = new StampedLock();


    @Autowired
    FloorDrainProperties properties;

    public FloorDrainMapCache() {
        /*
         * 5分钟清理一次过期缓存
         */
        CacheScheduler.getInstance().schedule(this::clear, DEFAULT_CLEAR_CACHE_DELAY, DEFAULT_CLEAR_CACHE_UNIT);
    }

    @Override
    public void set(String key, Integer value, long delay, TimeUnit unit) {
        Assert.notNull(key, "The object argument [key] must be null");
        Assert.notNull(value, "The object argument [value] must be null");
        Assert.notNull(unit, "The object argument [unit] must be null");
        long stamp = lock.writeLock(); // 获取写锁
        try {
            STORE.put(key, new CacheObject(value, delay, unit));
            CacheScheduler.getInstance().schedule(() -> del(key), delay, unit);

        } finally {
            lock.unlockWrite(stamp); // 释放写锁
        }
    }

    @Override
    public void set(String key, Integer value) {
        Assert.notNull(key, "The object argument [key] must be null");
        Assert.notNull(value, "The object argument [value] must be null");
        long stamp = lock.writeLock(); // 获取写锁
        long delay = properties.getInterval();
        TimeUnit unit = TimeUnit.MILLISECONDS;
        try {
            CacheObject cacheObj = STORE.get(key);
            if (null == cacheObj) {
                STORE.put(key, new CacheObject(value, delay, unit));
                CacheScheduler.getInstance().schedule(() -> del(key), delay, unit);
            } else {
                cacheObj.setValue(value);
                STORE.put(key, cacheObj);
            }
        } finally {
            lock.unlockWrite(stamp); // 释放写锁
        }
    }

    @Override
    public CacheObject get(String key) {
        Assert.notNull(key, "The object argument [key] must be null");
        long stamp = lock.tryOptimisticRead(); // 获得一个乐观读锁
        CacheObject obj = null;

        obj = STORE.get(key);

        // 此处已读取到y，如果没有写入，读取是正确的(100,200)
        // 如果有写入，读取是错误的(100,400)
        if (!lock.validate(stamp)) { // 检查乐观读锁后是否有其他写锁发生
            stamp = lock.readLock(); // 获取一个悲观读锁
            try {
                obj = STORE.get(key);
            } finally {
                lock.unlockRead(stamp); // 释放悲观读锁
            }
        }
        return obj;
    }

    @Override
    public Boolean hasKey(String key) {
        Assert.notNull(key, "The object argument [key] must be null");
        long stamp = lock.tryOptimisticRead(); // 获得一个乐观读锁
        Boolean obj = null;

        obj = STORE.containsKey(key);

        // 此处已读取到y，如果没有写入，读取是正确的(100,200)
        // 如果有写入，读取是错误的(100,400)
        if (!lock.validate(stamp)) { // 检查乐观读锁后是否有其他写锁发生
            stamp = lock.readLock(); // 获取一个悲观读锁
            try {
                obj = STORE.containsKey(key);
            } finally {
                lock.unlockRead(stamp); // 释放悲观读锁
            }
        }
        return obj;
    }

    @Override
    public void del(String key) {
        Assert.notNull(key, "The object argument [key] must be null");
        long stamp = lock.writeLock(); // 获取写锁
        try {
            STORE.remove(key);
        } finally {
            lock.unlockWrite(stamp); // 释放写锁
        }
    }

    @Override
    public long getExpire(String key) {
        Assert.notNull(key, "The object argument [key] must be null");

        long stamp = lock.tryOptimisticRead(); // 获得一个乐观读锁
        CacheObject obj = null;
        long expire = 0;
        obj = STORE.get(key);

        // 此处已读取到y，如果没有写入，读取是正确的(100,200)
        // 如果有写入，读取是错误的(100,400)
        if (!lock.validate(stamp)) { // 检查乐观读锁后是否有其他写锁发生
            stamp = lock.readLock(); // 获取一个悲观读锁
            try {
                obj = STORE.get(key);
            } finally {
                lock.unlockRead(stamp); // 释放悲观读锁
            }
        }
        if (obj != null) {
            expire = obj.getExpire();

        }

        return expire;
    }

    @Override
    public int incrementAndGet(String key) {
        Assert.notNull(key, "The object argument [key] must be null");
        int value = 0;
        CacheObject cacheObj = get(key);
        if (null == cacheObj) {
            value = 1;
        } else {
            value = cacheObj.getValue() + 1;
        }
        this.set(key, value);
        return value;
    }

    @Override
    public void clear() {
        long stamp = lock.writeLock(); // 获取写锁
        try {
            Iterator<CacheObject> it = STORE.values().iterator();
            CacheObject cacheObj = null;
            while (it.hasNext()) {
                cacheObj = it.next();
                if (cacheObj.getExpire() <= 0) {
                    it.remove();
                }
            }
        } finally {
            lock.unlockWrite(stamp); // 释放写锁
        }

    }

    public FloorDrainProperties getProperties() {
        return properties;
    }

    public void setProperties(FloorDrainProperties properties) {
        this.properties = properties;
    }
}
