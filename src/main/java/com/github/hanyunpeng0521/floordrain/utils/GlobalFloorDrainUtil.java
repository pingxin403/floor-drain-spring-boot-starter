package com.github.hanyunpeng0521.floordrain.utils;

/**
 * 单例模式
 *
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain.utils
 * hyp create at 20-3-24
 **/
public enum GlobalFloorDrainUtil {
    /**
     * 单例
     */
    INSTANCE;

    private static final String cacheKeyPrefix = "floordrain_";
    private static final String blacklistKeyPrefix = "floordrain_blacklist_";

    public String getLockKey(String ip) {
        return formatKey(cacheKeyPrefix + ip);
    }

    public String getBlacklistKey(String ip) {
        return formatKey(blacklistKeyPrefix + ip);
    }

    public String formatKey(String key) {
        if (null == key || key.isEmpty()) {
            return null;
        }
        return key.replaceAll("[.:]", "_");
    }
}
