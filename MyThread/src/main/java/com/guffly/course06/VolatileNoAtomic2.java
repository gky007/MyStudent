package com.guffly.course06;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile不具备原子性
 */
public class VolatileNoAtomic2 extends Thread {
//    private static volatile int count;
    private static AtomicInteger count = new AtomicInteger(0);

    public static void addCount() {
	for (int i = 0; i < 1000; i++) {
	    count.incrementAndGet();
	}
	System.out.println(count);
    }

    public void run() {
		addCount();
    }

    // 最后一次不是1000，volatile不具备原子性
    public static void main(String[] args) {
	VolatileNoAtomic2[] arr = new VolatileNoAtomic2[10];
	for (int i = 0; i < 10; i++) {
	    arr[i] = new VolatileNoAtomic2();
	}

	for (int i = 0; i < 10; i++) {
	    arr[i].start();
	}
    }
}
