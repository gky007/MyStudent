package com.guffly.disruptor.demo2;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version 1.0.0
 * @description: 交易实体类
 * @author: Mr.G
 * @date: 2021-10-18 19:58
 **/
public class Trade {
    private String id;
    private String name;
    private double price;
    AtomicInteger count = new AtomicInteger(0);
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public AtomicInteger getCount() {
        return count;
    }

    public void setCount(AtomicInteger count) {
        this.count = count;
    }
}
