package com.github.hanyunpeng0521.floordrain.cache;

import com.github.hanyunpeng0521.floordrain.property.FloorDrainProperties;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain.cache
 * hyp create at 20-3-24
 **/

class FloorDrainMapCacheTest {

    @Test
    void test01() {
        FloorDrainProperties properties = new FloorDrainProperties();
        FloorDrainMapCache cache = new FloorDrainMapCache();
        cache.setProperties(properties);
        cache.set("123", 123, 1000, TimeUnit.MINUTES);
        cache.set("123", 123);
        System.out.println(cache.get("123"));
        System.out.println(cache.get("1234"));
        System.out.println(cache.hasKey("123"));
        System.out.println(cache.hasKey("1234"));

        System.out.println(cache.incrementAndGet("456"));

    }

}