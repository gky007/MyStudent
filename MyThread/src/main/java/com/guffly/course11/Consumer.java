package com.guffly.course11;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable{
    private BlockingQueue<MyTask> blockingQueue;
    
    public Consumer(BlockingQueue<MyTask> blockingQueue) {
	this.blockingQueue = blockingQueue;
    }

    public void run() {
	while(true) {
	    try {
		MyTask take = this.blockingQueue.take();
		Thread.sleep(new Random().nextInt(1000));
		System.out.println("当前线程："+Thread.currentThread().getName() + ", 消费了数据 id为："+ take.getId());
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
}
