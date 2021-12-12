package com.gky.io.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-23 21:21
 **/
public class NettyClientHandler extends ChannelHandlerAdapter {

    //当通道就绪了就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client ctx = " + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello sever: ~o( =∩ω∩= )m喵", CharsetUtil.UTF_8));
    }

    // 当通道有读取事件时，触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息: "+buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器ip: " + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
