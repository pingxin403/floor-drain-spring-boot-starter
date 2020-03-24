package com.github.hanyunpeng0521.floordrain.cache;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 缓存对象
 *
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain.cache
 * hyp create at 20-3-24
 **/
public class CacheObject implements Serializable {

    /**
     * 缓存值
     */
    private int value;
    /**
     * 过期时间
     */
    private long expire;

    public CacheObject() {
    }

    public CacheObject(int value, long expire, TimeUnit unit) {
        this.value = value;
        // 实际过期时间等于当前时间加上有效期
        this.expire = System.currentTimeMillis() + unit.toMillis(expire);
    }

    public CacheObject(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public CacheObject setValue(int value) {
        this.value = value;
        return this;
    }

    public long getExpire() {
        return expire - System.currentTimeMillis();
    }

    public CacheObject setExpire(long expire) {
        this.expire = expire;
        return this;
    }

    @Override
    public String toString() {
        return "CacheObject{" +
                "value=" + value +
                ", expire=" + expire +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheObject that = (CacheObject) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
