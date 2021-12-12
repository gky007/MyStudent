package com.guffly.disruptor.demo1;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @version 1.0.0
 * @description:    Disruptor3.0以后的事件转换器（填充事件的业务数据）
 * 很明显的是：当用一个简单队列来发布事件的时候会牵涉更多的细节，这是因为事件对象还需要预先创建。
 *
 * 发布事件最少需要两步：
 *
 * 获取下一个事件槽，发布事件（发布事件的时候要使用try/finnally保证事件一定会被发布）。
 *
 * 如果我们使用RingBuffer.next()获取一个事件槽，那么一定要发布对应的事件。如果不能发布事件，那么就会引起Disruptor状态的混乱。尤其是在多个事件生产者的情况下会导致事件消费者失速，从而不得不重启应用才能会恢复。
 *
 * Disruptor 3.0提供了lambda式的API。这样可以把一些复杂的操作放在Ring Buffer，所以在Disruptor3.0以后的版本最好使用Event Publisher或者Event Translator(事件转换器)来发布事件。
 *
 * 上面写法的另一个好处是，Translator可以分离出来并且更加容易单元测试。Disruptor提供了不同的接口(EventTranslator, EventTranslatorOneArg, EventTranslatorTwoArg, 等等)
 * 去产生一个Translator对象。很明显，Translator中方法的参数是通过RingBuffer来传递的。
 *
 * @author: Mr.G
 * @date: 2021-10-17 14:50
 **/
public class LongEventProducerWithTranslator {
    //一个translator可以看做一个事件初始化器，publicEvent方法会调用它
    //填充Event
    private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR =
            new EventTranslatorOneArg<LongEvent, ByteBuffer>() {
                public void translateTo(LongEvent event, long sequence, ByteBuffer bb) {
                    event.setValue(bb.getLong(0));
                }
            };

    private final RingBuffer<LongEvent> ringBuffer;
    public LongEventProducerWithTranslator(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer bb) {
        ringBuffer.publishEvent(TRANSLATOR, bb);
    }
}

