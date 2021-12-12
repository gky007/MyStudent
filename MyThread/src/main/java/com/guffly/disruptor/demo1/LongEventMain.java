package com.guffly.disruptor.demo1;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @version 1.0.0
 * @description: 组装起来
 * 最后一步就是把所有的代码组合起来完成一个完整的事件处理系统。Disruptor在这方面做了简化，
 * 使用了DSL风格的代码（其实就是按照直观的写法，不太能算得上真正的DSL）。虽然DSL的写法比较简单，
 * 但是并没有提供所有的选项。如果依靠DSL已经可以处理大部分情况了。
 *
 * 注意：这里没有使用时间转换器，而是使用简单的 事件发布器。
 *
 * @author: Mr.G
 * @date: 2021-10-17 14:52
 **/
public class LongEventMain {
    public static void main(String[] args) throws InterruptedException {
        // Executor that will be used to construct new threads for consumers
        ExecutorService executor = Executors.newCachedThreadPool();
        // The factory for the event
        LongEventFactory factory = new LongEventFactory();
        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;
        // Construct the Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(
                factory,
                bufferSize,
                executor,
                ProducerType.SINGLE, // 单一写者模式
                new BlockingWaitStrategy()  // Disruptor的等待策略
                );

        // 连接消费事件
        disruptor.handleEventsWith(new LongEventHandler());
        // 启动
        disruptor.start();
        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

//        LongEventProducer producer = new LongEventProducer(ringBuffer);
        LongEventProducerWithTranslator producer = new  LongEventProducerWithTranslator(ringBuffer);
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; l < 1000; l++) {
            bb.putLong(0, l);

            //发布事件
            producer.onData(bb);
//            Thread.sleep(1000);
        }
        disruptor.shutdown();
        executor.shutdown();

    }
}
