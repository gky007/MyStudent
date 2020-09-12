package com.guffly.course08;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/** 
 * 多线程
 * Dubble check 双重校验创建对象
 * 
 * @author guffly
 * @since 2020/09/12
 */
public class DubbleSingleton {
    private static DubbleSingleton ds;

    public static DubbleSingleton getDs() {
	if (ds == null) {
	    try {
		Thread.sleep(3000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    synchronized (DubbleSingleton.class) { // 持有锁对象的线程才能进入创建对象方法
		if (ds == null) { // 若不加校验。线程在抢夺资源等待的时候都去实例化了对象
		    ds = new DubbleSingleton();
		}
	    }
	}
	return ds;
    }

    public static void main(String[] args) throws InterruptedException {
	Thread t1 = new Thread(new Runnable() {
	    public void run() {
		System.out.println(DubbleSingleton.getDs().hashCode());
	    }
	}, "t1");
	
	Thread t2 = new Thread(new Runnable() {
	    public void run() {
		System.out.println(DubbleSingleton.getDs().hashCode());
	    }
	}, "t2");
	
	Thread t3 = new Thread(new Runnable() {
	    public void run() {
		System.out.println(DubbleSingleton.getDs().hashCode());
	    }
	}, "t3");
	t1.start();
	t2.start();
	t3.start();
    }
}