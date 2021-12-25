package com.guffly.course05;

import java.util.ArrayList;
import java.util.List;

/**
 * 线程交互
 *
 * @author guffly
 * @since 2020/09/12
 */
public class ListAdd1 {
    private volatile static List list = new ArrayList();

    public void add() {
		list.add("12323");
    }

    private int size() {
		return list.size();
    }

    public static void main(String[] args) {
	final ListAdd1 listAdd = new ListAdd1();

	Thread t1 = new Thread(new Runnable() {
	    public void run() {
		try {
		    for (int i = 0; i < 10; i++) {
				listAdd.add();
				System.out.println("当前线程：" + Thread.currentThread().getName() + "添加了一个元素。。。");
				Thread.sleep(500);
		    }
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		if (listAdd.size() == 5) {
		    System.out.println(" -----------------");
		}
	    }
	}, "t1");

	Thread t2 = new Thread(new Runnable() {
	    public void run() {
		while (true) {
		    if (listAdd.size() == 5) {
				System.out.println("当前线程：" + Thread.currentThread().getName() + "添加了一个元素。。。");
		    }
		}
	    }
	}, "t2");
	t1.start();
	t2.start();
    }
}
