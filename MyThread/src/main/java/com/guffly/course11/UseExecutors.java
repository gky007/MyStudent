package com.guffly.course11;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
/**
 * Executors�̳߳ص�ʹ��
 * */
public class UseExecutors {
    public static void main(String[] args) {
	//����10���̵߳��̳߳�
	ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
	ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
	ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
	ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1);
    }
}
