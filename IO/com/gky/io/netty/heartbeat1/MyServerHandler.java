package com.gky.io.netty.heartbeat1;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-24 18:46
 **/
public class MyServerHandler extends ChannelHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 将evt转型
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + " ---- 超时事件----" + eventType);
            System.out.println("服务器作相应处理。。。");
            // 空闲关闭通道
            ctx.channel().close();
        }
    }
}
