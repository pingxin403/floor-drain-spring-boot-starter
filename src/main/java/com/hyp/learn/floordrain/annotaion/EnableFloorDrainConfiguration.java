package com.hyp.learn.floordrain.annotaion;

import java.lang.annotation.*;

/**
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain.annotaion
 * hyp create at 20-3-24
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableFloorDrainConfiguration {
}
