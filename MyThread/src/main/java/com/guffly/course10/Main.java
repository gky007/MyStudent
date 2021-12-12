package com.guffly.course10;

import java.util.Random;
/**
 * master_worker设计模式
 * */
public class Main {
    public static void main(String[] args) {
	System.out.println("我的机器可用的process数量："+Runtime.getRuntime().availableProcessors());
	Master master = new Master(new Worker(), Runtime.getRuntime().availableProcessors());

	for (int i = 1; i <= 100; i++) {
	    Task t = new Task();
	    t.setId(i);
	    t.setName("任务" + i);
	    t.setPrice(new Random().nextInt(1000));
	    master.submit(t);
	}
	
	master.execute();
	
	long start = System.currentTimeMillis();
	while (true) {
	    if (master.isComplete()) {
		long end = System.currentTimeMillis() - start;
		int ret = master.getResult();
		System.out.println("最终结果：" + ret + "，耗时：" + end+"ms");
		break;
	    }
	}
    }
}
