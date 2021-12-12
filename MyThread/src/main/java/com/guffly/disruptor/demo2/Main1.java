package com.guffly.disruptor.demo2;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.YieldingWaitStrategy;
import sun.java2d.loops.GraphicsPrimitive;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-18 20:00
 **/
public class Main1 {
    public static final int BUFF_SIZE = 1024;
    public static final int THREAD_SIZE = 4;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /**
         * createSingleProducer创建一个单生产者ringBuffer
         * */
        final RingBuffer<Trade> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<Trade>() {
            @Override
            public Trade newInstance() {
                return new Trade();
            }
        }, BUFF_SIZE, new YieldingWaitStrategy());

        // 创建线程池
        ExecutorService executors = Executors.newFixedThreadPool(THREAD_SIZE);

        // 创建SequenceBarriere
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        // 创建消息处理
        BatchEventProcessor<Trade> tradeProcessor = new BatchEventProcessor<Trade>(
                ringBuffer,
                sequenceBarrier,
                new TradeHandler());

        // 这一步的目的就是把消费者的位置信息加入到生产者，如果只有一个消费者可以省略
        ringBuffer.addGatingSequences(tradeProcessor.getSequence());

        // 把对应的process提交
        executors.submit(tradeProcessor);

        // 生产者
        Future<Void> future = executors.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                long seq;
                for (int i = 0; i < 10; i++) {
                    seq = ringBuffer.next();
                    ringBuffer.get(seq).setPrice(Math.random() * 9999);
                    ringBuffer.publish(seq);
                }
                return null;
            }
        });

        future.get(); // 等待生产结束
        Thread.sleep(2000);
        tradeProcessor.halt(); // 通知事件处理器可以结束了
        executors.shutdown(); // 关闭线程池
    }
}
