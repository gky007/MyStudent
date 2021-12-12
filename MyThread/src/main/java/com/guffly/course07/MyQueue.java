package com.guffly.course07;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * �Զ������
 * 
 * @author guffly
 * @since 2020/09/12
 * */
public class MyQueue {
    // 1.��Ҫһ������Ԫ�صļ���
    private LinkedList<Object> list = new LinkedList<Object>();
    
    // 2.��Ҫһ��������
    private AtomicInteger count = new AtomicInteger(0);
    
    // 3.��Ҫ����������
    private final int minSize = 0;
    
    private final int maxSize;
    
    public MyQueue(int maxSize) {
	this.maxSize = maxSize;
    }
    
    // 5.��ʼ��������
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
	    // 1.����Ԫ��
	    list.add(obj);
	    System.out.println(Thread.currentThread().getName() + " 当前加入元素是 "+ obj);
	    // 2.�������ۼ�
	    count.incrementAndGet();
	    // 3.֪ͨ��һ���߳�
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
	    // 1.�Ƴ�Ԫ��
	    retObj = list.removeFirst();
	    // 2.��������һ
	    count.decrementAndGet();
	    System.out.println(Thread.currentThread().getName() + " 当前移除元素是 "+ retObj);
	    // 3.֪ͨ��һ���߳�
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
	final MyQueue myQueue = new MyQueue(5);
	myQueue.put("a");
	myQueue.put("b");
	myQueue.put("c");
	myQueue.put("d");
	myQueue.put("e");
	
	System.out.println("容器长度 "+myQueue.getSize());
	
	Thread t1 = new Thread(new Runnable() {
	    public void run() {
		myQueue.put("f");
		myQueue.put("g");
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
	    }
	}, "t2");
	Thread.sleep(4000);
	t2.start();
    }
}