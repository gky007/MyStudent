package com.guffly.course10;

import java.util.Random;
/**
 * master_worker场景使用，分配任务给worker，然后汇总给master
 * */
public class Main {
    public static void main(String[] args) {
	System.out.println("我的电脑线程数 ： "+Runtime.getRuntime().availableProcessors());
	Master master = new Master(new Worker(), 1000);

	for (int i = 1; i <= 2; i++) {
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
		System.out.println("最终结果：" + ret + " 执行时间：" + end);
		break;
	    }
	}
    }
}
