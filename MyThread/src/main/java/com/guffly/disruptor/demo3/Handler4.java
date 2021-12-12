package com.guffly.disruptor.demo3;

import com.guffly.disruptor.demo2.Trade;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-18 21:01
 **/
public class Handler4 implements EventHandler<Trade>, WorkHandler<Trade> {
    @Override
    public void onEvent(Trade trade, long l, boolean b) throws Exception {
        this.onEvent(trade);
    }

    @Override
    public void onEvent(Trade trade) throws Exception {
        System.out.println("handler4: get name "+trade.getName());
        trade.setName(trade.getName() + " -> h4");
//        Thread.sleep(1000);
    }
}
