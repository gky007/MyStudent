package com.guffly.disruptor.demo2;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.WorkProcessor;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.YieldingWaitStrategy;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-18 20:00
 **/
public class Main2 {
    public static final int BUFF_SIZE = 1024;
    public static final int THREAD_SIZE = 4;

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        EventFactory<Trade> eventFactory = new EventFactory<Trade>() {
            @Override
            public Trade newInstance() {
                return new Trade();
            }
        };

        RingBuffer<Trade> ringBuffer =RingBuffer.createSingleProducer(eventFactory, BUFF_SIZE);

        // 创建SequenceBarriere
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_SIZE);

        WorkHandler<Trade> handler = new TradeHandler();

        WorkerPool<Trade> workerPool = new WorkerPool<Trade>(
                ringBuffer,
                sequenceBarrier,
                new IgnoreExceptionHandler(),
                handler);

        workerPool.start(executor);
        for (int i = 0; i < 10; i++) {
            long seq = ringBuffer.next();
            ringBuffer.get(seq).setPrice(Math.random() * 9999);
            ringBuffer.publish(seq);
        }

        Thread.sleep(1000);
        workerPool.halt(); // 通知事件处理器可以结束了
        executor.shutdown(); // 关闭线程池
    }
}
