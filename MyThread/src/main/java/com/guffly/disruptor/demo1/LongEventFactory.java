package com.guffly.disruptor.demo1;

import com.lmax.disruptor.EventFactory;

/**
 * @version 1.0.0
 * @description: 由于需要让Disruptor为我们创建事件，我们同时还声明了一个EventFactory来实例化Event对象。
 * @author: Mr.G
 * @date: 2021-10-17 14:41
 **/
public class LongEventFactory implements EventFactory {
    @Override
    public Object newInstance() {
        return new LongEvent();
    }
}
