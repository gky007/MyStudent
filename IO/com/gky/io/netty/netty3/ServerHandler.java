package com.gky.io.netty.netty3;

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
        String request = (String)msg;
        System.out.println("Server: " + request);
        //写给客户端
        String response = "我是服务器响应数据$_";
        ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
//                .addListener(ChannelFutureListener.CLOSE); //只要发送一次就关闭连接
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
