package com.github.hanyunpeng0521.floordrain.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;

/**
 * Print the  LOGO.
 *
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain.context
 * hyp create at 20-3-24
 **/
@Order(LoggingApplicationListener.DEFAULT_ORDER)
public class FloorDrainLogoApplactionListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(FloorDrainLogoApplactionListener.class);

    private static Banner.Mode mode = Banner.Mode.CONSOLE;

    public static Banner.Mode getMode() {
        return mode;
    }

    public static void setMode(Banner.Mode customMode) {
        mode = customMode;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (mode == Banner.Mode.OFF) {
            return;
        }

        String bannerText = FloorDrainBanner.buildBannerText();
        if (mode == Banner.Mode.CONSOLE) {
            System.out.println(bannerText);
        } else {
            log.info(bannerText);
        }
    }
}
