package com.guffly.course11;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

public class MyRejected implements RejectedExecutionHandler {

    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
	 System.out.println("自定义处理..");
	 System.out.println("当前被拒绝任务为：" + r.toString());
    }

}
