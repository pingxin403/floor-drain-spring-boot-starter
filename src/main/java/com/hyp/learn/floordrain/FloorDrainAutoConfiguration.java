package com.hyp.learn.floordrain;

import com.hyp.learn.floordrain.annotaion.EnableFloorDrainConfiguration;
import com.hyp.learn.floordrain.cache.Cache;
import com.hyp.learn.floordrain.cache.FloorDrainMapCache;
import com.hyp.learn.floordrain.cache.FloorDrainRedisCache;
import com.hyp.learn.floordrain.property.FloorDrainProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain
 * hyp create at 20-3-24
 **/
@Configuration
@ConditionalOnBean(annotation = EnableFloorDrainConfiguration.class)
@EnableConfigurationProperties(FloorDrainProperties.class)
public class FloorDrainAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(FloorDrainAutoConfiguration.class);
    @Autowired
    FloorDrainProperties properties;

    @PostConstruct
    public void init() {
        log.info("Floor Drain has been turned on! Best wishes for you! ");
        log.info("You'll be safe with Floor Drain... ");
    }

    @Bean
    @ConditionalOnMissingBean(name = {"floorDrainProcessor"})
    FloorDrainProcessor FloorDrainProcessor() {
        return new FloorDrainShieldProcessor();
    }

    @Bean(name = "floorDrainCache")
    Cache floorDrainCache() {
        FloorDrainCacheType type = properties.getType();
        if (type == FloorDrainCacheType.REDIS) {
            log.info("Enabling Floor Drain cache: [Redis]");
            return new FloorDrainRedisCache();
        }
        log.info("Enabling Floor Drain cache: [Map]");
        return new FloorDrainMapCache();
    }
}
