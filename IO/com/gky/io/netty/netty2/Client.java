package com.gky.io.netty.netty2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-20 22:31
 **/
public class Client {
    public static void main(String[] args) throws Exception {

        EventLoopGroup workgroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workgroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        // 设置自定义分隔符
                        ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
                        sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
                        //设置字符串编码
                        sc.pipeline().addLast(new StringDecoder());
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });

        ChannelFuture cf1 = b.connect("127.0.0.1", 8765).sync();

        //buf
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("客户端发送aaa$_".getBytes()));
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("客户端发送信息BBBB$_".getBytes()));
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("客户端发送信息B222222$_".getBytes()));
        cf1.channel().closeFuture().sync();
        workgroup.shutdownGracefully();

    }
}
