package com.guffly.course01;

/**
 * 线程回顾synchronized关键字
 * 
 * @author guffly
 * @since 2020/09/12
 * */
public class MyThread extends Thread{
    private int count = 5;
    
    public synchronized void run() {
	count --;
	System.out.println(this.currentThread().getName() + "count = " + count);
    }
    
    public static void main(String[] args) {
	/**
	 * 分析：当多线程访问myThread的run方法时，以排队的方式进行处理。（这里的排队按照cpu先后顺序而定的）
	 * 	一个线程想要执行synchronized修饰的方法里的代码：
	 * 	1.尝试获得锁
	 * 	2.如果拿到，执行代码里的内容，拿不到锁，这个线程会不断尝试获取这把锁，直到拿到为止，
	 * 	  而且多线程会去竞争这把锁。（也许是有锁竞争的问题）
	 * */
	MyThread myThread= new MyThread();
	Thread t1 = new Thread(myThread, "t1 ----- ");
	Thread t2 = new Thread(myThread, "t2 ----- ");
	Thread t3 = new Thread(myThread, "t3 ----- ");
	Thread t4 = new Thread(myThread, "t4 ----- ");
	Thread t5 = new Thread(myThread, "t5 ----- ");
	t1.start();
	t2.start();
	t3.start();
	t4.start();
	t5.start();
    }
}
