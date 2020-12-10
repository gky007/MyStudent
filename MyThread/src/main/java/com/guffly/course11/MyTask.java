package com.guffly.course11;

public class MyTask implements Runnable {
    private int id;
    private String name;

    public void run() {
	System.out.println("任务id= " + id + " 任务名称= " + name);
	try {
	    Thread.sleep(5*1000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    public MyTask(int id, String name) {
	this.id = id;
	this.name = name;
    }

    /**
     * @return the id
     */
    public int getId() {
	return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
	this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return "MyTask [id=" + id + ", name=" + name + "]";
    }

}
