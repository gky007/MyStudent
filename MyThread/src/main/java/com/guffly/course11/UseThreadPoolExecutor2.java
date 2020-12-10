package com.guffly.course11;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class UseThreadPoolExecutor2 implements Runnable {
    private static AtomicInteger atomicInteger = new AtomicInteger();
    public void run() {
	int count = atomicInteger.incrementAndGet();
	System.out.println("����" + count);
	try {
	    Thread.sleep(2000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) throws InterruptedException {
//	BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
	BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10);
	ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
		5, 			//coreSize �ȴ���һ���߳���
		10, 			//MaxSize  ��ӵ��н�����У����������ִֵ�оܾ���������׳��쳣����������µ��߳�
		120L, 			//60
		TimeUnit.SECONDS,	
		queue); // �н����
	for (int i = 0; i < 20; i++) {
	    threadPoolExecutor.execute(new UseThreadPoolExecutor2());
	}
	Thread.sleep(1000);
	System.out.println("queue size : "+queue.size());
	Thread.sleep(2000);
    }
}
