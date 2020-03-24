package com.github.hanyunpeng0521.floordrain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hanyunpeng0521.floordrain.annotaion.EnableFloorDrainConfiguration;
import com.github.hanyunpeng0521.floordrain.property.FloorDrainProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain
 * hyp create at 20-3-24
 **/
@Configuration
@ConditionalOnBean(annotation = EnableFloorDrainConfiguration.class)
@EnableConfigurationProperties(FloorDrainProperties.class)
@ConditionalOnProperty(name = "floordrain.limit.access.type", havingValue = "redis")
public class FloorDrainRedisAutoConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
