package com.guffly.course10;

import java.util.Random;

public class MyWorker extends Worker{
    public static Object handle(Task input) {
	Object output = null;
	try {
	    //��ʾ����ҵ��ĺ�ʱ����ѯ���߲������ݿ�
	    Thread.sleep(500);
	    int srNo = new Random().nextInt(1000);
	    System.out.println("������ʽ���ɹ�������Ϊ��"+ srNo);
	    output = input.getPrice();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	return output;
    }
}
