package com.guffly.course06;

/**
 * volatile
 * 
 * @author guffly
 * @since 2020/09/12
 */
public class RunThread2 extends Thread{
	private static volatile boolean isRunning;

    public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
    }

	public static boolean isIsRunning() {
		return isRunning;
	}

	public void run() {
		System.out.println("进入run方法。。。");
		while(isRunning = true) {
			// TODO
		}
		System.out.println("线程方法停止ֹ");
    }

    public static void main(String[] args) throws InterruptedException {
		RunThread2 rt = new RunThread2();
		rt.start();
		Thread.sleep(3000);
		rt.setRunning(false);
		System.out.println("isRunning的值已经被设置了false");
		Thread.sleep(1000);
		System.out.println(rt.isRunning);
    }
}
