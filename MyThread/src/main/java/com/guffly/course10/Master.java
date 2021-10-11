package com.guffly.course10;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

import com.sun.org.apache.xpath.internal.compiler.Compiler;

public class Master {
    // 1. ��װ����ļ���
    private ConcurrentLinkedQueue<Object> workQueue = new ConcurrentLinkedQueue<Object>();
    
    // 2. ʹ��HashMap��������Worker����
    private HashMap<String, Thread> workers = new HashMap<String, Thread>();
    
    // 3. ʹ��һ��������װûһ��worker��ִ������Ľ����
    private ConcurrentHashMap<String, Object> resultMap = new ConcurrentHashMap<String, Object>();
    
    // 4 . ���췽��
    public Master(Worker worker, int workerCount) {
	// ÿһ��worker������Ҫ��master�����ã�workQueue�����������ȡ��resultMap����������ύ
	worker.setWorkerQueue(this.workQueue);
	worker.setResultMap(this.resultMap);
	for (int i = 0; i < workerCount; i++) {
	    //key��ʾÿ��worker�����֣�Value��ʾÿһ���̶߳���
	    workers.put("�ӽڵ�"+Integer.toString(i), new Thread(worker));
	}
    }
    
    // 5. �ύ����
    public void submit(Task task) {
//	System.out.println(task.getName());
	this.workQueue.add(task);
    }
    
    // 6.��Ҫ��һ��ִ�еķ���������Ӧ�ó��������е�Ӧ�ù�����
    public void execute () {
	for(Map.Entry<String, Thread> me : workers.entrySet()) {
	    me.getValue().start();
	}
    }

    // 8. �ж��߳��Ƿ�ִ�����
    public boolean isComplete() {
	for(Map.Entry<String, Thread> me : workers.entrySet()) {
	    if(me.getValue().getState() != Thread.State.TERMINATED) {
		return false;
	    }
	}
	return true;
    }

    // 9.���ؽ����������
    public int getResult() {
	int ret = 0;
	for(Map.Entry<String, Object> me : resultMap.entrySet()) {
	    // ������Ϣ
	    System.out.println("me:"+me.toString());
	    ret += (Integer) me.getValue();
	}
	return ret;
    }
}