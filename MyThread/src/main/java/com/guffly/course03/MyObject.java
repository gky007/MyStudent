package com.guffly.course03;

/**
 * �̻߳عˣ�
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
    // ע��run�������˳��
    public static void main(String[] args) {
	final MyObject mo = new MyObject();

	/**
	 * ����
	 * t1��������object�����lock����t2�߳̿����첽�ķ�ʽ���ö����еķ�synchronize���εķ���
	 * t1��������object�����lock����t2�߳���������ʱ����ö����е�ͬ����synchronize����������ȴ���Ҳ����ͬ��
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
