package com.hyp.learn.floordrain.property;

import com.hyp.learn.floordrain.FloorDrainCacheType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * 可配置属性
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain.property
 * hyp create at 20-3-24
 **/
@ConfigurationProperties("floordrain.limit.access")
public class FloorDrainProperties {

    /**
     * 连续访问最高阀值，超过该值则认定为恶意操作的IP
     * 单位：次 默认为20
     */
    private int threshold = 20;

    /**
     * 间隔时间，在该时间内如果访问次数大于阀值，则记录为恶意IP，否则视为正常访问
     * 单位：毫秒(ms)，默认为 5秒
     */
    private long interval = 5000;

    /**
     * 限制访问的容错值，容错值范围内(0 < x < faultTolerance)过了限制时间就可正常访问，一旦大于容错值，则进行限制访问
     * 默认为-1，表示不进行直接限制
     */
    private int faultTolerance = -1;

    /**
     * 当检测到恶意访问时，对恶意访问的ip进行限制的时间
     * 单位：毫秒(ms)，默认为 1分钟
     */
    private long limitedTime = 60000;

    /**
     * 黑名单存在的时间，在单位时间内用户访问受限的次数累加
     * 单位：毫秒(ms)，默认为 1个月
     */
    private long blacklistTime = 2592000000L;

    /**
     * 缓存类型，默认为map存储
     */
    private FloorDrainCacheType type = FloorDrainCacheType.MAP;

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public int getFaultTolerance() {
        return faultTolerance;
    }

    public void setFaultTolerance(int faultTolerance) {
        this.faultTolerance = faultTolerance;
    }

    public long getLimitedTime() {
        return limitedTime;
    }

    public void setLimitedTime(long limitedTime) {
        this.limitedTime = limitedTime;
    }

    public long getBlacklistTime() {
        return blacklistTime;
    }

    public void setBlacklistTime(long blacklistTime) {
        this.blacklistTime = blacklistTime;
    }

    public FloorDrainCacheType getType() {
        return type;
    }

    public FloorDrainProperties setType(String type) {
        if (StringUtils.isEmpty(type)) {
            return this;
        }
        this.type = FloorDrainCacheType.valueOf(type.toUpperCase());
        return this;
    }
}
