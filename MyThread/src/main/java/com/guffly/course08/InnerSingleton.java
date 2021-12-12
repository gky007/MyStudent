package com.guffly.course08;

/** 
 * Dubble check 
 * 
 * @author guffly
 * @since 2020/09/12
 */
public class InnerSingleton {
    private  static class Singleton {	
	private static Singleton single = new Singleton();
    }
    
    public static Singleton getSingleton() {
	return Singleton.single;
    }
    
    public static void main(String[] args) {
	Thread t1 = new Thread(new Runnable() {
	    public void run() {
		System.out.println(InnerSingleton.getSingleton().hashCode());
	    }
	}, "t1");
	
	Thread t2 = new Thread(new Runnable() {
	    public void run() {
		System.out.println(InnerSingleton.getSingleton().hashCode());
	    }
	}, "t2");
	
	Thread t3 = new Thread(new Runnable() {
	    public void run() {
		System.out.println(InnerSingleton.getSingleton().hashCode());
	    }
	}, "t3");
	t1.start();
	t2.start();
	t3.start();
    }
}
