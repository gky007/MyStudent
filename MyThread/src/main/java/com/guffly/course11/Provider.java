package com.guffly.course11;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Provider implements Runnable {
    private BlockingQueue<MyTask> blockingQueue;
    
    private static AtomicInteger atomicInteger = new AtomicInteger();

	public void setRunning(boolean running) {
		isRunning = running;
	}

	private volatile boolean isRunning = true;

    public Provider(BlockingQueue<MyTask> blockingQueue) {
		this.blockingQueue = blockingQueue;
    }

    public void run() {
		while (isRunning) {
			try {
				Thread.sleep(new Random().nextInt(1000));
				int id = atomicInteger.incrementAndGet();
				MyTask myTask = new MyTask(id, "任务" + id);
				System.out.println("生产者： " + Thread.currentThread().getName() + ", 任务id " + id + " ");
				if (!this.blockingQueue.offer(myTask, 2, TimeUnit.SECONDS)) {
					System.out.println("提交数据到缓冲区失败。。。。");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
    }

    public void stop() {
		this.setRunning(false);
    }
}
