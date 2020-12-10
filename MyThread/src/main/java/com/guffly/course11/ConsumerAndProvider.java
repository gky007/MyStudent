package com.guffly.course11;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ConsumerAndProvider {
    // 创建一个线程池
    public static void main(String[] args) throws InterruptedException {

	BlockingQueue<MyTask> blockingQueue = new LinkedBlockingQueue<MyTask>(10);

	// 生产者
	Provider p1 = new Provider(blockingQueue);
	Provider p2 = new Provider(blockingQueue);
	Provider p3 = new Provider(blockingQueue);

	// 消费者
	Consumer c1 = new Consumer(blockingQueue);
	Consumer c2 = new Consumer(blockingQueue);
	Consumer c3 = new Consumer(blockingQueue);
	ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	cachedThreadPool.execute(p1);
	cachedThreadPool.execute(p2);
	cachedThreadPool.execute(p3);
	cachedThreadPool.execute(c1);
	cachedThreadPool.execute(c2);
	cachedThreadPool.execute(c3);
	Thread.sleep(3000);
	p1.stop();
	p2.stop();
	p3.stop();
	Thread.sleep(2000);
    }
}
