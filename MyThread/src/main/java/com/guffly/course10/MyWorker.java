package com.guffly.course10;

import java.util.Random;

public class MyWorker extends Worker{
    public static Object handle(Task input) {
	Object output = null;
	//task任务很耗时
	int srNo = new Random().nextInt(1000);
//	System.out.println("单号：" + srNo);
	output = input.getPrice();
	return output;
    }
}
