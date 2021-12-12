package com.gky.io.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-24 17:37
 **/
public class GroupChatServerHandler extends ChannelHandlerAdapter {
    // 定义一个channel组，管理所有的channel
    // GlobalEventExecutor.INSTANCE全局事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 表示建立连接，一旦连接，第一个被执行
    // 当前channel 加入到channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将聊天的信息推送给在线的客户端
        channelGroup.add(channel);
        /**
         * 该方法会将channelGroup中的channel遍历发消息
         * */
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 加入聊天 "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +"\n");
    }

    //断开连接，推送给在线的客户端
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 离开了\n");
        System.out.println("channelGroup size = " + channelGroup.size());
    }

    // 表示channel处于活动状态， 提示xxx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 离开");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush("[客户]" + ch.remoteAddress() + " 发送消息: " + msg + "\n");
            } else {
                ch.writeAndFlush("[我] 发送消息: " + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
