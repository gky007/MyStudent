package com.guffly.disruptor.demo4;

import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.YieldingWaitStrategy;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA_2_3.portable.OutputStream;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;


/**
 * @version 1.0.0
 * @description: 、
 * @author: Mr.G
 * @date: 2021-10-18 22:48
 **/
public class Main {
    public static void main(String[] args) throws InterruptedException {
        RingBuffer<Order> ringBuffer =RingBuffer.createSingleProducer(
                Order::new,
                1024*1024,
                new YieldingWaitStrategy());
        SequenceBarrier barrier = ringBuffer.newBarrier();
        Consumer[] consumers = new Consumer[3];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer("c" + i);
        }
        WorkerPool<Order> workerPool = new WorkerPool<Order>(
                ringBuffer,
                barrier,
                new IntEventExceptionHandler(),
                consumers
        );
        // sequenceBarrier将设置到ringBuffer中进行协调，生产者生产过快。
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
        final CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 100; i++) {
            final Producer p = new Producer(ringBuffer);
            new Thread(()->{
               try{
                   latch.await();
               }catch (InterruptedException e){
                   e.printStackTrace();
               }
                for (int j = 0; j < 100; j++) {
                    p.onData(UUID.randomUUID().toString().replace("-",""));
                }
            }).start();
        }

        Thread.sleep(2000);
        System.out.println("=================开始生产==================");
        latch.countDown();
        Thread.sleep(5000);
        System.out.println("总数： "+ consumers[0].getCount());

    }

    static class IntEventExceptionHandler implements ExceptionHandler {

        @Override
        public void handleEventException(Throwable throwable, long l, Object o) {

        }

        @Override
        public void handleOnStartException(Throwable throwable) {

        }

        @Override
        public void handleOnShutdownException(Throwable throwable) {

        }
    }
}
