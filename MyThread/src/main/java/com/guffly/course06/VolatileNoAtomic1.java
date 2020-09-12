package com.guffly.course06;
/**
 * volatile���߱�ԭ���Բ������synchronized
 * */
public class VolatileNoAtomic1 extends Thread{
    private static volatile int count;
    
    public static void addCount() {
	for (int i = 0; i < 1000; i++) {
	    count++;
	}
	System.out.println(count);
    }
    
    public void run() {
	addCount();
    }

    // ���һ�β���10000��volatile���߱�ԭ����
    public static void main(String[] args) {
	VolatileNoAtomic1[] arr = new VolatileNoAtomic1[10];
	for (int i = 0; i < 10; i++) {
	    arr[i] = new VolatileNoAtomic1();
	}
	
	for (int i = 0; i < 10; i++) {
	    arr[i].start();
	}
    }
}
