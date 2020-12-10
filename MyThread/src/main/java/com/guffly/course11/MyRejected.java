package com.guffly.course11;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

public class MyRejected implements RejectedExecutionHandler {

    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
	System.out.println("拒绝策略执行了。。。");
	System.out.println("当前任务被拒绝了"+r.toString());
    }

}
