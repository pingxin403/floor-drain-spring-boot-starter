package com.github.hanyunpeng0521.floordrain.cache;

import com.github.hanyunpeng0521.floordrain.property.FloorDrainProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * 缓存实现--Redis
 *
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain.cache
 * hyp create at 20-3-24
 **/
public class FloorDrainRedisCache implements Cache {
    @Autowired
    @Qualifier("floorDrainRedisTemplate")
    RedisTemplate<String, CacheObject> redisTemplate;
    @Autowired
    FloorDrainProperties properties;


    @Override
    public void set(String key, Integer value, long delay, TimeUnit unit) {
        Assert.notNull(key, "The object argument [key] must be null");
        Assert.notNull(value, "The object argument [value] must be null");
        Assert.notNull(unit, "The object argument [unit] must be null");
        redisTemplate.opsForValue().set(key, new CacheObject(value), delay, unit);
    }

    @Override
    public void set(String key, Integer value) {
        Assert.notNull(key, "The object argument [key] must be null");
        Assert.notNull(value, "The object argument [value] must be null");

        ValueOperations operations = redisTemplate.opsForValue();
        CacheObject cacheObj = (CacheObject) operations.get(key);
        if (null == cacheObj) {
            this.set(key, value, properties.getInterval(), TimeUnit.MILLISECONDS);
        } else {
            operations.set(key, cacheObj.setValue(value), getExpire(key), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public CacheObject get(String key) {
        Assert.notNull(key, "The object argument [key] must be null");
        return (CacheObject) redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean hasKey(String key) {
        Assert.notNull(key, "The object argument [key] must be null");
        return redisTemplate.hasKey(key);
    }

    @Override
    public void del(String key) {
        Assert.notNull(key, "The object argument [key] must be null");
        if (hasKey(key)) {
            redisTemplate.delete(key);
        }
    }

    @Override
    public long getExpire(String key) {
        Assert.notNull(key, "The object argument [key] must be null");
        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    @Override
    public int incrementAndGet(String key) {
        Assert.notNull(key, "The object argument [key] must be null");
        int value = 0;
        CacheObject obj = this.get(key);
        if (null == obj) {
            value = 1;
        } else {
            value = obj.getValue() + 1;
        }
        this.set(key, value);
        return value;
    }
}
