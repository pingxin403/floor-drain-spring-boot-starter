package com.hyp.learn.floordrain.execption;

/**
 *异常
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain.execption
 * hyp create at 20-3-24
 **/
public class GlobalFloorDrainCacheException extends RuntimeException {
    public GlobalFloorDrainCacheException() {
        super();
    }

    public GlobalFloorDrainCacheException(String message) {
        super(message);
    }

    public GlobalFloorDrainCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlobalFloorDrainCacheException(Throwable cause) {
        super(cause);
    }

    protected GlobalFloorDrainCacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
