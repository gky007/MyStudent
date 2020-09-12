package com.guffly.course09;

import java.util.Iterator;
import java.util.concurrent.*;

import com.sun.org.apache.xpath.internal.compiler.Compiler;

public class Queue {
    // ConcurrentLinkedQueue队列无阻塞队列
    private static void ConcurrentLinkedQueue() {
	ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<String>();
	concurrentLinkedQueue.add("a");
	concurrentLinkedQueue.add("b");
	concurrentLinkedQueue.add("c");
	concurrentLinkedQueue.add("d");
	concurrentLinkedQueue.offer("e");
	System.out.println(concurrentLinkedQueue.poll());
	System.out.println(concurrentLinkedQueue.size());
	System.out.println(concurrentLinkedQueue.peek());
	System.out.println(concurrentLinkedQueue.size());
	System.out.println("=====ConcurrentLinkedQueue队列------");
    }

    // ArrayBlockingQueue有界队列
    private static void ArrayBlockingQueue() throws InterruptedException {
	ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(5);
	arrayBlockingQueue.put("a");
	arrayBlockingQueue.put("b");
	arrayBlockingQueue.add("c");
	arrayBlockingQueue.add("d");
	arrayBlockingQueue.add("e");
	for (Iterator iterator = arrayBlockingQueue.iterator(); iterator.hasNext();) {
	    Object object = (Object) iterator.next();
	    System.out.println(object);
	}
	System.out.println("=====ArrayBlockingQueue队列------");
    }

    // LinkedBlockingQueue阻塞队列
    private static void LinkedBlockingQueue() {
	LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<String>();
	linkedBlockingQueue.offer("a");
	linkedBlockingQueue.offer("b");
	linkedBlockingQueue.offer("c");
	linkedBlockingQueue.offer("d");
	linkedBlockingQueue.add("e");
	for (Iterator iterator = linkedBlockingQueue.iterator(); iterator.hasNext();) {
	    Object object = (Object) iterator.next();
	    System.out.println(object);
	}
	System.out.println("=====LinkedBlockingQueue队列------");
    }

    // SynchronousQueue 访问数据量非常少
    private static void SynchronousQueue() {
	SynchronousQueue<String> synchronousQueue = new SynchronousQueue<String>();
	synchronousQueue.add("e32");
	System.out.println("=====SynchronousQueue队列------");
    }

    private static void PriorityBlockingQueue() {
	PriorityBlockingQueue<Task> priorityBlockingQueue = new PriorityBlockingQueue<Task>();
	Task task1 = new Task();
	task1.setId(1);
	task1.setName("任务1");
	Task task2 = new Task();
	task2.setId(3);
	task2.setName("任务2");
	Task task3 = new Task();
	task3.setId(2);
	task3.setName("任务3");
	priorityBlockingQueue.add(task1);
	priorityBlockingQueue.add(task2);
	priorityBlockingQueue.add(task3);
	for (Iterator iterator = priorityBlockingQueue.iterator(); iterator.hasNext();) {
	    Task task = (Task) iterator.next();
	    System.out.println(" taskId = " + task.getId() + " taskNnme = " + task.getName());
	}
	System.out.println("=====PriorityBlockingQueue队列------");
    }

    public static void main(String[] args) throws InterruptedException {
	ConcurrentLinkedQueue();
	ArrayBlockingQueue();
	LinkedBlockingQueue();
//	SynchronousQueue();
	PriorityBlockingQueue();
	DelayQueue();
    }

    public static class Task implements Comparable<Task> {
	public int id;

	public String name;

	public int getId() {
	    return id;
	}

	public void setId(int id) {
	    this.id = id;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public int compareTo(Task task) {
	    return this.id > task.id ? 1 : (this.id < task.id ? -1 : 0);
	}
    }

    public static class Wangmin implements Delayed {
	private String name;

	private String id;

	private long endTime;

	public TimeUnit timeUnit = TimeUnit.SECONDS;

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getId() {
	    return id;
	}

	public void setId(String id) {
	    this.id = id;
	}

	public long getEndTime() {
	    return endTime;
	}

	public void setEndTime(long endTime) {
	    this.endTime = endTime;
	}

	public Wangmin(String name, String id, long endTime) {
	    this.name = name;
	    this.id = id;
	    this.endTime = endTime;
	}

	public long getDelay(TimeUnit unit) {
	    return endTime - System.currentTimeMillis();
	}

	public int compareTo(Delayed delayed) {
	    Wangmin w = (Wangmin)delayed;
	    return this.getDelay(this.timeUnit) -w.getDelay(this.timeUnit) > 0 ? 1 : 0;
	}
    }
    
    public static class WangBa implements Runnable {
	private DelayQueue<Wangmin> delayQueue = new DelayQueue<Wangmin>();
	
	public boolean yinye = true;
	
	public void ShangJi(String name, String id, int money) {
	    Wangmin wangmin = new Wangmin(name, id, 1000*money+System.currentTimeMillis());
	    System.err.println("网名" + wangmin.getName() + " 身份证：" + wangmin.getId() + " 交钱：" + money + " 开始上网。。。");
	    this.delayQueue.add(wangmin);
	}
	
	public void XiaJi(Wangmin wangmin) {
	    System.err.println("网名" + wangmin.getName() + " 身份证：" + wangmin.getId() + " 下机。。。");
	}
	
	public void run() {
	    while(yinye) {
		try {
		    Wangmin take = delayQueue.take();
		    XiaJi(take);
		} catch (Exception e) {
		}
	    }
	}
    }
    
    private static void DelayQueue() {
	System.err.println("网吧开始营业");
	WangBa wangBa = new WangBa();
	Thread sw = new Thread(wangBa);
	sw.start();
	
	wangBa.ShangJi("A", "612422124324", 1);
	wangBa.ShangJi("B", "423661242212", 5);
	wangBa.ShangJi("C", "361242212437", 10);
    }
}