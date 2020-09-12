package com.guffly.course04;

/**
 * 线程回顾： 数据的脏读，同一对象调用getset方法须在这两个方法上加上synchronized关键字
 * 
 * @author guffly
 * @since 2020/09/12
 */
public class DirtyRead {
    private String username = "dgwgyet";

    private String password = "243tgdf";

    public synchronized void setValue(String username, String password) {
	this.username = username;
	try {
	    Thread.sleep(2000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	this.password = password;
	System.out.println("setValue最终结果：username = " + username + " password = " + password);
    }

    public synchronized void getValue() {
	System.out.println("getValue获得的值：username = " + username + " password = " + password);
    }

    public synchronized void method2() {
	System.out.println(Thread.currentThread().getName());
    }

    // 注意run方法输出顺序
    public static void main(String[] args) throws InterruptedException {
	final DirtyRead dr = new DirtyRead();
	Thread t1 = new Thread(new Runnable() {
	    public void run() {
		dr.setValue("guffly", "123456");
	    }
	});
	t1.start();
	Thread.sleep(1000);
	dr.getValue();
    }
}
