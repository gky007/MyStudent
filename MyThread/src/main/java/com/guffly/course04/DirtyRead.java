package com.guffly.course04;

/**
 * �̻߳عˣ� ���ݵ������ͬһ�������getset�������������������ϼ���synchronized�ؼ���
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
	System.out.println("setValue���ս����username = " + username + " password = " + password);
    }

    public synchronized void getValue() {
	System.out.println("getValue��õ�ֵ��username = " + username + " password = " + password);
    }

    public synchronized void method2() {
	System.out.println(Thread.currentThread().getName());
    }

    // ע��run�������˳��
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
