package com.guffly.course10;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Worker implements Runnable{
    
    private ConcurrentLinkedQueue<Object> workQueue;
    
    private ConcurrentHashMap<String, Object> resultMap;
    
    public void setWorkerQueue(ConcurrentLinkedQueue<Object> workQueue) {
	this.workQueue = workQueue;
    }

    public void setResultMap(ConcurrentHashMap<String, Object> resultMap) {
	this.resultMap = resultMap;
    }
    
    public void run() {
	while(true) {
	    Task input = (Task)this.workQueue.poll();
	    if (input == null) {
		break;
	    }
	    // 真正的做业务处理
//	    try {
//		Thread.sleep(1000);
//	    } catch (InterruptedException e) {
//		e.printStackTrace();
//	    }
	    Object output = MyWorker.handle(input);
	    this.resultMap.put(Integer.toString(input.getId()), output);
	}
    }
    
//    private Object handle(Task input) {
//	Object output = null;
//	try {
//	    //task任务很耗时
//	    Thread.sleep(500);
//	    output = input.getPrice();
//	} catch (InterruptedException e) {
//	    e.printStackTrace();
//	}
//	return output;
//    }
}
