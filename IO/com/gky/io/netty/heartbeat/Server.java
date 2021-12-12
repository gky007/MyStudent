package com.gky.io.netty.heartbeat;

import com.gky.io.netty.netty4.MarshallingCodeCFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-21 23:49
 **/
public class Server {
    public Server() {
    }

    public static void main(String[] args) throws Exception {
        EventLoopGroup pGroup = new NioEventLoopGroup();
        EventLoopGroup cGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        ((ServerBootstrap)((ServerBootstrap)((ServerBootstrap)b.group(pGroup, cGroup).channel(NioServerSocketChannel.class)).option(ChannelOption.SO_BACKLOG, 1024)).handler(new LoggingHandler(LogLevel.INFO))).childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel sc) throws Exception {
                sc.pipeline().addLast(new ChannelHandler[]{MarshallingCodeCFactory.buildMarshallingDecoder()});
                sc.pipeline().addLast(new ChannelHandler[]{MarshallingCodeCFactory.buildMarshallingEncoder()});
                sc.pipeline().addLast(new ChannelHandler[]{new ServerHeartBeatHandler()});
            }
        });
        ChannelFuture cf = b.bind(8765).sync();
        cf.channel().closeFuture().sync();
        pGroup.shutdownGracefully();
        cGroup.shutdownGracefully();
    }
}
