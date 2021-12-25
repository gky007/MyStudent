package com.guffly.course11;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Temp extends Thread {
    int i = 1;
    public void run() {
	    System.out.println("run..." + i++);
    }
}
public class ScheduledJob {
    public static void main(String[] args) {
        Temp temp = new Temp();
        ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1);
        newScheduledThreadPool.scheduleWithFixedDelay(temp, 1, 1, TimeUnit.SECONDS);
    }
}
