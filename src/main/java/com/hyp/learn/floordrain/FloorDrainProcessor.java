package com.hyp.learn.floordrain;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain
 * hyp create at 20-3-24
 **/
public interface FloorDrainProcessor {
    FloorDrainResponse process(HttpServletRequest request);
}
