package com.guffly.course05;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * �߳̽���
 * 
 * @author guffly
 * @since 2020/09/12
 */
public class ListAdd3 {
    private volatile static List list = new ArrayList();

    public void add() {
	list.add("12323");
    }

    private int size() {
	return list.size();
    }

    public static void main(String[] args) {
	final ListAdd3 listAdd2 = new ListAdd3();

	// ʵ��������һ��lock
	// ��ʹ��wait��notify��ʱ��һ��Ҫ���synchronized�ؼ���
//	final Object lock = new Object();
	final CountDownLatch countDownLatch = new CountDownLatch(1);
	
	Thread t1 = new Thread(new Runnable() {
	    public void run() {
		try {
//		    synchronized (lock) {
			for (int i = 0; i < 10; i++) {
			    listAdd2.add();
			    System.out.println("��ǰ�̣߳�" + Thread.currentThread().getName() + "�����һ��Ԫ�ء�����");
			    Thread.sleep(500);
			    if (listAdd2.size() == 5) {
//				lock.notify();
				countDownLatch.countDown();
				System.out.println("�ѷ���֪ͨ������");
			    }
			}
//		    }
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}

	    }
	}, "t1");

	Thread t2 = new Thread(new Runnable() {
	    public void run() {
//		  synchronized (lock) {
		    if (listAdd2.size() != 5) {
			try {
//			    lock.wait();
			    countDownLatch.await();
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		    }
		    System.out.println("��ǰ�̣߳�" + Thread.currentThread().getName() + "�����һ��Ԫ�ء�����");
		    throw new RuntimeException();
//		  }
	    }
	}, "t2");
	t2.start();
	t1.start();
    }
}