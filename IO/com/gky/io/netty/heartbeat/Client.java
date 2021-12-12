package com.gky.io.netty.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-21 23:52
 **/
public class Client {
    public Client() {
    }

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        ((Bootstrap)((Bootstrap)b.group(group)).channel(NioSocketChannel.class)).handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel sc) throws Exception {
                sc.pipeline().addLast(new ChannelHandler[]{MarshallingCodeCFactory.buildMarshallingDecoder()});
                sc.pipeline().addLast(new ChannelHandler[]{MarshallingCodeCFactory.buildMarshallingEncoder()});
                sc.pipeline().addLast(new ChannelHandler[]{new ClienHeartBeattHandler()});
            }
        });
        ChannelFuture cf = b.connect("127.0.0.1", 8765).sync();
        cf.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}
