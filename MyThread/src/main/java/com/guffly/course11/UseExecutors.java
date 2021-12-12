package com.guffly.course11;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
/**
 * Executors线程池
 * */
public class UseExecutors {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
		//线程池
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
//		ExecutorService newFixedThreadPool = null;
//		ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
//		ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
//		ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1);
//		for (int i = 0; i < 10; i++) {
//			int finalI = i;
//			Executors.newFixedThreadPool(10).submit(new Runnable() {
//				@Override
//				public void run() {
//					System.out.println("newScheduledThreadPool " + finalI + newScheduledThreadPool.toString());
//				}
//			});
//		}
//		Thread.sleep(1000);
//		newFixedThreadPool.shutdown();
		Future<Object> call = newFixedThreadPool.submit(new Callable<Object>() {
			@Override
			public String call() throws Exception {
				MyTask call = new MyTask(1, "call");
				Thread.sleep(2000);
				return "haha";
			}
		});
		System.out.println("call.get() = " + call.get());
		newFixedThreadPool.shutdown();
	}
}
