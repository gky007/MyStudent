package com.guffly.course01;

/**
 * �̻߳ع�synchronized�ؼ���
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
	 * �����������̷߳���myThread��run����ʱ�����Ŷӵķ�ʽ���д�����������ŶӰ���cpu�Ⱥ�˳������ģ�
	 * 	һ���߳���Ҫִ��synchronized���εķ�����Ĵ��룺
	 * 	1.���Ի����
	 * 	2.����õ���ִ�д���������ݣ��ò�����������̻߳᲻�ϳ��Ի�ȡ�������ֱ���õ�Ϊֹ��
	 * 	  ���Ҷ��̻߳�ȥ�������������Ҳ�����������������⣩
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
