package com.guffly.course11;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UseThreadPoolExecutor3 {
    public static void main(String[] args) {
	ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
		1, 			//coreSize �ȴ���һ���߳���
		2, 			//MaxSize  ��ӵ��н�����У����������ִֵ�оܾ���������׳��쳣����������µ��߳�
		60, 			//60
		TimeUnit.SECONDS,	
		new ArrayBlockingQueue<Runnable>(3),
		new MyRejected()); // �н����
	MyTask mt1 = new MyTask(1, "����1");
	MyTask mt2 = new MyTask(2, "����2");
	MyTask mt3 = new MyTask(3, "����3");
	MyTask mt4 = new MyTask(4, "����4");
	MyTask mt5 = new MyTask(5, "����5");
	MyTask mt6 = new MyTask(6, "����6");
	MyTask mt7 = new MyTask(7, "����7");
	threadPoolExecutor.execute(mt1);
	threadPoolExecutor.execute(mt2);
	threadPoolExecutor.execute(mt3);
	threadPoolExecutor.execute(mt4);
	threadPoolExecutor.execute(mt5);
	threadPoolExecutor.execute(mt6);
	threadPoolExecutor.execute(mt7);
	threadPoolExecutor.shutdown();
    }
}
