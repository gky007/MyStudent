package com.guffly.disruptor.demo1;

/**
 * @version 1.0.0
 * @description: 首先定义一个Event来包含需要传递的数据
 * @author: Mr.G
 * @date: 2021-10-17 14:40
 **/
public class LongEvent {
    private long value;
    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
