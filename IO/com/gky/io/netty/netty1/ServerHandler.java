package com.gky.io.netty.netty1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-20 22:32
 **/
public class ServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //do something msg
        ByteBuf buf = (ByteBuf)msg;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        String request = new String(data, "utf-8");
        System.out.println("Server: " + request);
        //写给客户端
        String response = "我是服务器反馈的信息: hello 客户端";
        ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
//                .addListener(ChannelFutureListener.CLOSE); //只要发送一次就关闭连接
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
