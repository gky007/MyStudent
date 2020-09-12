package com.guffly.course03;

/**
 * 线程回顾：
 * 
 * @author guffly
 * @since 2020/09/12
 */
public class MyObject {
    public synchronized void method1() {
	try {
	    System.out.println(Thread.currentThread().getName());
	    Thread.sleep(4000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }
    
    public synchronized void method2() {
	System.out.println(Thread.currentThread().getName());
    }
    // 注意run方法输出顺序
    public static void main(String[] args) {
	final MyObject mo = new MyObject();

	/**
	 * 分析
	 * t1对象会持有object对象的lock锁，t2线程可以异步的方式调用对象中的非synchronize修饰的方法
	 * t1对象会持有object对象的lock锁，t2线程如果在这个时候调用对象中的同步（synchronize）方法则需等待，也就是同步
	 * */
	Thread t1 = new Thread(new Runnable() {
	    public void run() {
		mo.method1();
	    }
	}, "t1");
	Thread t2 = new Thread(new Runnable() {
	    public void run() {
		mo.method2();
	    }
	}, "t2");
	t1.start();
	t2.start();
    }
}
