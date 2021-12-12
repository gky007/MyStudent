package com.gky.io.netty.netty2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @version 1.0.0
 * @description:
 * SYN 表示建立连接
 * FIN 表示关闭连接
 * ACK 表示响应
 * PSH 表示有 DATA数据传输
 * RST 表示连接重置。
 * 第一次握手: 客户端向服务器端发送SYN标志的包建立连接时，客户端发送syn包(syn=j)到服务器，并进入SYN_SEND状态，等待服务器确认；
 * 第二次握手: 服务器端，向客户端发送 SYN 和ACK的包 服务器收到syn包，必须确认客户的SYN（ack=j+1），同时自己也发送一个SYN包（syn=k），
 *          即SYN+ACK包，此时服务器进入SYN_RECV状态
 * 第三次握手: 客户端向服务器端，收到服务端发送的SYN和ACK包，确认正确后，给服务器发送发送ACK的包, 客户端收到服务器的SYN＋ACK包，
 *          向服务器发送确认包ACK(ack=k+1)，此包发送完毕，客户端和服务器进入ESTABLISHED状态，完成三次握手。完成三次握手，
 *          客户端与服务器开始传送数据。
 * @author: Mr.G
 * @date: 2021-10-20 22:32
 **/
public class Server {
    public static void main(String[] args) throws Exception {
        //1 第一个线程组 是用于接收Client端连接的
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //2 第二个线程组 是用于实际的业务处理操作的
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //3 创建一个辅助类Bootstrap，就是对我们的Server进行一系列的配置
        ServerBootstrap b = new ServerBootstrap();
        //把俩个工作线程组加入进来
        b.group(bossGroup, workerGroup)
                //我要指定使用NioServerSocketChannel这种类型的通道
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024) //设置tcp缓冲区大小
                .option(ChannelOption.SO_SNDBUF, 1024) //设置发送缓冲区大小
                .option(ChannelOption.SO_RCVBUF, 1024) //设置接受缓冲区大小
                .option(ChannelOption.SO_KEEPALIVE, true) //设置tcp长连接
                //一定要使用 childHandler 去绑定具体的 事件处理器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        // 设置特殊分隔符
                        ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
                        sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
                        //设置字符串编码
                        sc.pipeline().addLast(new StringDecoder());
                        sc.pipeline().addLast(new ServerHandler());
                    }
                });

        //绑定指定的端口 进行监听
        ChannelFuture f = b.bind(8765).sync();

        //Thread.sleep(1000000);
        f.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
