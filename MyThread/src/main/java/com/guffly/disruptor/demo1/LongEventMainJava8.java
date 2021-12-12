package com.guffly.disruptor.demo1;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @version 1.0.0
 * @description: 在Java 8使用Disruptor
 * Disruptor在自己的接口里面添加了对于Java 8 Lambda的支持。大部分Disruptor中的接口都符合Functional
 * Interface的要求（也就是在接口中仅仅有一个方法）。所以在Disruptor中，可以广泛使用Lambda来代替自定义类。
 * @author: Mr.G
 * @date: 2021-10-17 14:54
 **/
public class LongEventMainJava8 {
    /**
     * 用lambda表达式来注册EventHandler和EventProductor
     * @param args
     * @throws InterruptedException
     */public static void main(String[] args) throws InterruptedException {
        // Executor that will be used to construct new threads for consumers
        Executor executor = Executors.newCachedThreadPool();
        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;// Construct the Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, executor);
        // 可以使用lambda来注册一个EventHandler
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> System.out.println("Event: " + event.getValue()));
        // Start the Disruptor, starts all threads running
        disruptor.start();
        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        LongEventProducer producer = new LongEventProducer(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);for (long l = 0; true; l++) {
            bb.putLong(0, l);
            ringBuffer.publishEvent((event, sequence, buffer) -> event.setValue(buffer.getLong(0)), bb);
            Thread.sleep(1000);
        }
    }
}
