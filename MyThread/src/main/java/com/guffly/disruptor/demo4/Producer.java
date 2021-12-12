package com.guffly.disruptor.demo4;

import com.lmax.disruptor.RingBuffer;
import com.sun.corba.se.impl.presentation.rmi.ExceptionHandler;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA_2_3.portable.OutputStream;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-18 22:59
 **/
public class Producer {
    private final RingBuffer<Order> ringBuffer;

    public Producer(RingBuffer<Order> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(String data){
        long sequence = ringBuffer.next();
        try{
            Order order = ringBuffer.get(sequence);
            order.setId(data);
        }finally {
            ringBuffer.publish(sequence);
        }
    }

}
