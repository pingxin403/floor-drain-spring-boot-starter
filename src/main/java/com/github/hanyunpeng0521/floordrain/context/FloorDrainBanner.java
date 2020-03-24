package com.github.hanyunpeng0521.floordrain.context;

/**
 * 自定义banner，启动时显示
 *
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain.context
 * hyp create at 20-3-24
 **/
public class FloorDrainBanner {
    public static final String INIT_VERSION = "1.0.0";

    public static final String LOGO =
            "___ _                ___           _      \n" +
                    "| __| |___  ___ _ _  |   \\ _ _ __ _(_)_ _  \n" +
                    "| _|| / _ \\/ _ \\ '_| | |) | '_/ _` | | ' \\ \n" +
                    "|_| |_\\___/\\___/_|   |___/|_| \\__,_|_|_||_|";

    public static String buildBannerText() {
        return System.getProperty("line.separator") + LOGO + " :: Floor Drain ::        (v" + FloorDrainVersion.getVersion(INIT_VERSION) + ")" + System.getProperty("line.separator");
    }
}
