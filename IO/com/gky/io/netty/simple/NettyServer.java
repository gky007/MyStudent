package com.gky.io.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-23 20:07
 **/
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 1。创建两个线程 bossGroup和 workGroup
         * 2. bossGroup只是处理连接请求，真正的和客户端交互交给workGroup完成
         * 3. 两个都是无限循环
         * 4. bossGroup和 workGroup含有的线程个数默认是cpu核数*2
         */
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup(8);

        try {
            //创建服务器端启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程进行设置
            bootstrap.group(bossGroup, workGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用NioServerSocketChannel作为服务器的通道
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列的连接数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动长连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道初始化对象
                        // 给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(new NettyServerHandler());
                        }
                    }); // 给workeGroup 的 EventLoop 对应的管道设置处理器

            System.out.println("...服务器 is ready...");

            // 绑定一个端口并且同步，生成了一个ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(6668).sync();

            // 给cf注册监听器
            cf.addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("监听端口 6668 成功");
                } else {
                    System.out.println("监听端口 6668 失败");
                }
            } );

            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
