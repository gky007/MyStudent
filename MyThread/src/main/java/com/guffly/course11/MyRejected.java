package com.guffly.course11;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

public class MyRejected implements RejectedExecutionHandler {

    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
	System.out.println("�ܾ�����ִ���ˡ�����");
	System.out.println("��ǰ���񱻾ܾ���"+r.toString());
    }

}
