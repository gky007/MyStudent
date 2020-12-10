package com.guffly.course11;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UseThreadPoolExecutor3 {
    public static void main(String[] args) {
	ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
		1, 			//coreSize 先创建一个线程数
		2, 			//MaxSize  添加到有界队列中，若大于最大值执行拒绝策略则会抛出异常，继续添加新的线程
		60, 			//60
		TimeUnit.SECONDS,	
		new ArrayBlockingQueue<Runnable>(3),
		new MyRejected()); // 有界队列
	MyTask mt1 = new MyTask(1, "任务1");
	MyTask mt2 = new MyTask(2, "任务2");
	MyTask mt3 = new MyTask(3, "任务3");
	MyTask mt4 = new MyTask(4, "任务4");
	MyTask mt5 = new MyTask(5, "任务5");
	MyTask mt6 = new MyTask(6, "任务6");
	MyTask mt7 = new MyTask(7, "任务7");
	threadPoolExecutor.execute(mt1);
	threadPoolExecutor.execute(mt2);
	threadPoolExecutor.execute(mt3);
	threadPoolExecutor.execute(mt4);
	threadPoolExecutor.execute(mt5);
	threadPoolExecutor.execute(mt6);
	threadPoolExecutor.execute(mt7);
	threadPoolExecutor.shutdown();
    }
}
