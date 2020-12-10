package com.guffly.course11;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
/**
 * Executors线程池的使用
 * */
public class UseExecutors {
    public static void main(String[] args) {
	//创建10个线程的线程池
	ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
	ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
	ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
	ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1);
    }
}
