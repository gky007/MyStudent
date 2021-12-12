package com.gky.io.netty.heartbeat1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-24 18:35
 **/
public class MyServer {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            /**long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit
                             * 1. IdleStateHandler是netty提供的的空闲处理器
                             * 2. readerIdleTime 表示多长时间没有读，就会发一个心跳检测包检测是否连接
                             * 3。 writerIdleTime 表示多长时间没有读，就会发一个心跳检测包检测是否连接
                             * 4. allIdleTime 表示多长时间没有读，就会发一个心跳检测包检测是否连接
                             *
                             * 5. 当IdleStateHandler触发后，就会传递给下一个管道handler去处理
                             * 通过调用下一个handler的userEventTiggered， 在读方法中去处理IdleStateEvent（读空闲，写空闲，读写空闲）
                             * */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            // 加入一个对空闲检测的自定义handler
                            pipeline.addLast(new MyServerHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(8889).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
