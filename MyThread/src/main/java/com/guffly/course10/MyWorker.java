package com.guffly.course10;

import java.util.Random;

public class MyWorker extends Worker{
    public static Object handle(Task input) {
	Object output = null;
	try {
	    //表示处理业务的耗时，查询或者操作数据库
	    Thread.sleep(500);
	    int srNo = new Random().nextInt(1000);
	    System.out.println("创建正式单成功，单号为："+ srNo);
	    output = input.getPrice();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	return output;
    }
}
