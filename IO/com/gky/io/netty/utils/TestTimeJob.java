package com.gky.io.netty.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-22 19:49
 **/
public class TestTimeJob {
    public TestTimeJob() {
    }

    public static void main(String[] args) throws Exception {
        Temp command = new Temp();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(command, 2L, 3L, TimeUnit.SECONDS);
    }
}