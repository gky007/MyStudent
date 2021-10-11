package com.guffly.course10;

import java.util.Random;

public class MyWorker extends Worker{
    public static Object handle(Task input) {
	Object output = null;
	// 表示处理业务的耗时，查询或者操作数据库
	int srNo = new Random().nextInt(1000);
	System.out.println("创建正式单成功，单号为：" + srNo);
	output = input.getPrice();
	return output;
    }
}
