package com.guffly.disruptor.demo3;

import com.guffly.disruptor.demo2.Trade;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import javafx.scene.control.RadioMenuItem;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-18 21:17
 **/
public class TradePublisher implements Runnable {
    private Disruptor<Trade> disruptor;
    private CountDownLatch latch;
    private static int LOOP = 1; // 模拟百万次交易
    public TradePublisher(CountDownLatch latch, Disruptor<Trade> disruptor) {
        this.disruptor = disruptor;
        this.latch = latch;
    }

    @Override
    public void run() {
        for (int i = 0; i < LOOP; i++) {
            disruptor.publishEvent(new ThreadEventTranslator());
        }
        latch.countDown();
    }

    class ThreadEventTranslator implements EventTranslator<Trade> {
        private Random random = new Random();

        @Override
        public void translateTo(Trade trade, long l) {
            this.generaterTrade(trade);
        }

        private Trade generaterTrade(Trade trade) {
            trade.setPrice(random.nextDouble() * 9999);
            return trade;
        }
    }
}
