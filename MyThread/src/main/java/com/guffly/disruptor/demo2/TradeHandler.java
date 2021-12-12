package com.guffly.disruptor.demo2;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import java.sql.SQLOutput;
import java.util.UUID;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-18 20:12
 **/
public class TradeHandler implements EventHandler<Trade>, WorkHandler<Trade> {

    @Override
    public void onEvent(Trade trade, long l, boolean b) throws Exception {
        this.onEvent(trade);
    }

    @Override
    public void onEvent(Trade trade) throws Exception {
        trade.setId(UUID.randomUUID().toString().replace("-",""));
        System.out.println(trade.getId());
    }
}
