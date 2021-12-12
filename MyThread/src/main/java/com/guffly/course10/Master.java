package com.guffly.course10;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

import com.sun.org.apache.xpath.internal.compiler.Compiler;

public class Master {
    // 1.  承装任务的集合
    private ConcurrentLinkedQueue<Object> workQueue = new ConcurrentLinkedQueue<Object>();
    
    // 2. 使用HashMap承装所有work对象
    private HashMap<String, Thread> workers = new HashMap<String, Thread>();
    
    // 3. 使用一个容器承装所有的work并执行所有的结果集
    private ConcurrentHashMap<String, Object> resultMap = new ConcurrentHashMap<String, Object>();
    
    // 4 . 构造方法
    public Master(Worker worker, int workerCount) {
	// 每一个work对象都由master引用
	worker.setWorkerQueue(this.workQueue);
	worker.setResultMap(this.resultMap);
	for (int i = 0; i < workerCount; i++) {
	    //key表示每一个work的名字Value每一个线程执行对象
	    workers.put("子节点"+Integer.toString(i), new Thread(worker));
	}
    }
    
    // 5. 提交方法
    public void submit(Task task) {
//	System.out.println(task.getName());
	this.workQueue.add(task);
	System.out.println(task.getName());
    }
    
    // 6.需要有一个执行方法（启动应用程序 让所有的work工作）
    public void execute () {
	for(Map.Entry<String, Thread> me : workers.entrySet()) {
	    me.getValue().start();
	    System.out.println(me.getKey());
	}
    }

    // 8. 判断是否执行完毕
    public boolean isComplete() {
	for(Map.Entry<String, Thread> me : workers.entrySet()) {
	    if(me.getValue().getState() != Thread.State.TERMINATED) {
		return false;
	    }
	}
	return true;
    }

    // 9.返回结果集数据
    public int getResult() {
	int ret = 0;
	for(Map.Entry<String, Object> me : resultMap.entrySet()) {
	    // 汇总逻辑
	    ret += (Integer) me.getValue();
	}
	return ret;
    }
}