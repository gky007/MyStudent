package com.guffly.disruptor.demo3;

import com.guffly.disruptor.demo2.Trade;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.EventProcessorFactory;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-18 21:01
 **/
public class Handler1 implements EventHandler<Trade>, WorkHandler<Trade> {
    @Override
    public void onEvent(Trade trade, long l, boolean b) throws Exception {
        this.onEvent(trade);
    }

    @Override
    public void onEvent(Trade trade) throws Exception {
        System.out.println("handler1: set name");
        trade.setName("h1");
//        Thread.sleep(1000);
    }
}
