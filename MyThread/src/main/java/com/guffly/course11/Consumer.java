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
				MyTask task = this.blockingQueue.take();
				Thread.sleep(new Random().nextInt(1000));
				System.out.println("消费者 "+Thread.currentThread().getName() + ", 任务id："+ task.getTaskId());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
}
