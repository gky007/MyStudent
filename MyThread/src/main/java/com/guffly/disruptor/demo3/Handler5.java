package com.guffly.disruptor.demo3;

import com.guffly.disruptor.demo2.Trade;
import com.lmax.disruptor.EventHandler;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-18 21:10
 **/
public class Handler5 implements EventHandler<Trade>{
    @Override
    public void onEvent(Trade trade, long l, boolean b) throws Exception {
        System.out.println("handler5: get price " + trade.getPrice());
        trade.setPrice(trade.getPrice() + 3.0);
//        Thread.sleep(1000);
    }
}

