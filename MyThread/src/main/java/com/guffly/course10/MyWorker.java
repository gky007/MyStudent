package com.guffly.course10;

import java.util.Random;

public class MyWorker extends Worker{
    public static Object handle(Task input) {
	Object output = null;
	// ��ʾ����ҵ��ĺ�ʱ����ѯ���߲������ݿ�
	int srNo = new Random().nextInt(1000);
	System.out.println("������ʽ���ɹ�������Ϊ��" + srNo);
	output = input.getPrice();
	return output;
    }
}
