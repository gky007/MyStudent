package com.gky.io.netty.groupchat;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-24 18:07
 **/
public class GroupChatClientHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg.toString().trim());
    }
}
