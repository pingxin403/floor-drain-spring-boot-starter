package com.github.hanyunpeng0521.floordrain.cache;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 缓存调度器,单例模式
 *
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain.cache
 * hyp create at 20-3-24
 **/
public class CacheScheduler {

    //新建线程的名称
    private AtomicInteger cacheTaskNumber = new AtomicInteger(1);

    //定期定时任务线程池
    private ScheduledExecutorService scheduler;

    private CacheScheduler() {
        this.shutdown();
        //重写新建线程方法
        this.scheduler = new ScheduledThreadPoolExecutor(10, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("Floor-Drain-Task-%s", cacheTaskNumber.getAndIncrement()));
            }
        });
    }

    private void shutdown() {
        if (null != scheduler) {
            this.scheduler.shutdown();
        }
    }

    public ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) {
        return this.scheduler.schedule(task, delay, unit);
    }


    //内部类实现单例模式
    public static final CacheScheduler getInstance() {
        return CacheSchedulerHandler.INSTANCE;
    }

    private static class CacheSchedulerHandler {
        private static final CacheScheduler INSTANCE = new CacheScheduler();
    }
}
