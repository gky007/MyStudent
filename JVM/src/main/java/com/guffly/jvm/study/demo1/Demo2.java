package com.guffly.jvm.study.demo1;

import org.openjdk.jol.info.ClassLayout;

/**
 * @version 1.0.0
 * @description:
 * 需要注意的是其中对象头保存的hashCode被称为identityHashCode，当我们调用对象的hashCode方法，
 * 返回的就是该值。若我们重写了hashCode的值，对象头的hashCode值仍然是内部的identityHashCode，
 * 而不是我们重写的hashCode值。可以通过System.identityHashCode打印identityHashCode，
 * 或者也可以通过toString直接打印对象输出16进制的identityHashCode。
 *
 * @author: Mr.G
 * @date: 2021-10-17 13:53
 **/
public class Demo2 {
    public static void main(String[] args) {
        Object obj1 = new Object();
        System.out.println(ClassLayout.parseInstance(obj1).toPrintable());
        System.out.println(obj1.hashCode());
        System.out.println(System.identityHashCode(obj1));
        System.out.println(ClassLayout.parseInstance(obj1).toPrintable());
        System.out.println(obj1);
    }
}
