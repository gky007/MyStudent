package com.guffly.jvm.study.demo1;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

/**
 * @version 1.0.0
 * @description:    打印基本对象的内部信息
 * @author: Mr.G
 * @date: 2021-10-17 13:42
 **/
public class Demo1 {
    public volatile long p1, p2, p3, p4, p5, p6 = 7L;
    public volatile String p11="这是放弃啊晚固定工位过去而我国切勿确认却无法上发发发";
    public static void main(String[] args) {
        Demo1 o = new Demo1();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        int[] ints = new int[5];
        ints[1] = 1;
        ints[2] = 2;
        System.out.println(ClassLayout.parseInstance(ints).toPrintable());
        synchronized (o) {
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }

//        //查看对象外部信息
//		System.out.println(GraphLayout.parseInstance(o).toPrintable());
//		//获取对象总大小
//		System.out.println("size : " + GraphLayout.parseInstance(o).totalSize());

    }
    public long sumPaddingToPreventOptimization() {
        return p1 + p2 + p3 + p4 + p5 + p6;
    }
}
