package com.guffly.disruptor.demo1;

import com.lmax.disruptor.EventHandler;

/**
 * @version 1.0.0
 * @description: 定义事件处理器（disruptor会回调此处理器的方法）
 * 我们还需要一个事件消费者，也就是一个事件处理器。这个事件处理器简单地把事件中存储的数据打印到终端：
 * @author: Mr.G
 * @date: 2021-10-17 14:44
 **/
public class LongEventHandler implements EventHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
        // 调用第三方接口
        System.out.println(" 调用第三方接口 ");
        System.out.println(longEvent.getValue());
    }
}
