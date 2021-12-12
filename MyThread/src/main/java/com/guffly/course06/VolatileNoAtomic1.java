package com.guffly.course06;
/**
 * volatile不具备原子性不能替代synchronized
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

	// 最后一次不是10000，volatile不具备原子性
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
