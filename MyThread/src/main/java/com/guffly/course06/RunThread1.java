package com.guffly.course06;

import java.util.ArrayList;
import java.util.List;

/**
 * volatile
 * 
 * @author guffly
 * @since 2020/09/12
 */
public class RunThread1 extends Thread{
    private boolean isRunning = true;

    public void setRunning(boolean isRunning) {
	this.isRunning = isRunning;
    }
    
    public void run() {
	System.out.println("����run����������");
	while(isRunning = true) {
	    // TODO
	}
	System.out.println("�̷߳���ֹͣ");
    }

    public static void main(String[] args) throws InterruptedException {
	RunThread1 rt = new RunThread1();
	rt.start();
	Thread.sleep(3000);
	rt.setRunning(false);
	System.out.println("isRunning��ֵ�Ѿ���������false");
	Thread.sleep(1000);
	System.out.println(rt.isRunning);
    }
}
