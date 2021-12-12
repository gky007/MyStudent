package com.guffly.disruptor.demo4;

import com.lmax.disruptor.WorkHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-18 22:50
 **/
public class Consumer implements WorkHandler<Order> {
    private String customerId;

    private static AtomicInteger count = new AtomicInteger(0);

    public AtomicInteger getCount() {
        return count;
    }

    public Consumer(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public void onEvent(Order order) throws Exception {
        System.out.println("当前消费者是: " + customerId + ", 消费信息: "+ order.getId());
        count.incrementAndGet();
    }
}
