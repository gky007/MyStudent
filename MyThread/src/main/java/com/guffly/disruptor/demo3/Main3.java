package com.guffly.disruptor.demo3;

import com.guffly.disruptor.demo2.Trade;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @version 1.0.0
 * @description: 六边形，但生产者，多消费者
 * @author: Mr.G
 * @date: 2021-10-18 20:51
 **/
public class Main3 {
    public static final int BUFFER_SIZE = 1024;
    public static final int THREAD_SIZE = 8;

    public static void main(String[] args) throws InterruptedException {
        long beginTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_SIZE);
        Disruptor disruptor = new Disruptor(
                Trade::new,
                BUFFER_SIZE,
                executor,
                ProducerType.SINGLE,
                new BusySpinWaitStrategy());
        // 六边形模式
        Handler1 h1 = new Handler1();
        Handler2 h2 = new Handler2();
        Handler3 h3 = new Handler3();
        Handler4 h4 = new Handler4();
        Handler5 h5 = new Handler5();
        disruptor.handleEventsWith(h1, h2);
        disruptor.after(h1).handleEventsWith(h4);
        disruptor.after(h2).handleEventsWith(h5);
        disruptor.after(h1, h2).handleEventsWith(h3);
        disruptor.start();
        CountDownLatch latch = new CountDownLatch(1);
        executor.submit(new TradePublisher(latch, disruptor));
        latch.await();
        Thread.sleep(1000);
        disruptor.shutdown();
        executor.shutdown();
        System.out.println("总共耗时： "+ (System.currentTimeMillis() - beginTime) + "ms");
    }
}
