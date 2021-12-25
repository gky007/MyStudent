package com.guffly.course07;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义队列
 * 
 * @author guffly
 * @since 2020/09/12
 * */
public class MyQueue {
    // 1.需要一个承载元素的集合
    private LinkedList<Object> list = new LinkedList<Object>();
    
    // 2.需要一个计数器
    private AtomicInteger count = new AtomicInteger(0);
    
    // 3.需要设置上下限
    private final int minSize = 0;
    
    private final int maxSize;
    
    public MyQueue(int maxSize) {
	this.maxSize = maxSize;
    }

	// 5.初始化锁对象
    private final Object lock = new Object();
    
    public void put(Object obj) {
	synchronized (lock) {
	    while (count.get() == this.maxSize) {
			try {
				lock.wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 1.加入元素
		list.add(obj);
		System.out.println(Thread.currentThread().getName() + " 当前加入元素是 "+ obj);
		// 2.计数器累加
		count.incrementAndGet();
		// 3.通知另一个线程
		lock.notify();
	}
    }
    
    //
    public Object take() {
		Object retObj = null;
		synchronized (lock) {
			while (count.get() == this.minSize) {
				try {
					lock.wait();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 1.移除元素
			retObj = list.removeFirst();
			// 2.计数器减一
			count.decrementAndGet();
			System.out.println(Thread.currentThread().getName() + " 当前移除元素是 "+ retObj);
			// 3.通知另一个线程
			lock.notify();
		}
		return retObj;
    }
    
    public LinkedList<Object> getList() {
	return list;
    }
    
    public int getSize() {
	return this.count.get();
    }
    public static void main(String[] args) throws InterruptedException {
		final MyQueue myQueue = new MyQueue(3);
		myQueue.put("a");
		myQueue.put("b");
		myQueue.put("c");

		System.out.println("当前容器长度："+myQueue.getSize());
	
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				myQueue.put("d");
				for (Object string : myQueue.getList()) {
					System.out.print(string.toString());
				}
			}
		}, "t1");
		t1.start();
	
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				Object o1 = myQueue.take();
				Object o2 = myQueue.take();
				Object o3 = myQueue.take();
				Object o4 = myQueue.take();
			}
		}, "t2");
		Thread.sleep(4000);
		t2.start();
    }
}