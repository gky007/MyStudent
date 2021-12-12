package com.gky.io.netty.simple;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-23 20:55
 **/
public class NettyServerHandler extends ChannelHandlerAdapter {

    //读取实际数据（可以读取客户端发送的消息）
    /**
     * 1.ChannlHandlerContext ctx: 上下文对象， 含有pipeline, 通道channel， 地址
     * 2. Object msg: 客户端发送的数据 默认Object
     * */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //比如这里 有个非常耗时的业务 -> 异步执行 -> 提交到channel对应的NIOEventLoop的taskQueue中

        // 解决放案1 用户自定义线程普通任务
        ctx.channel().eventLoop().execute(()->{
            try {
                Thread.sleep(10 * 1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端 ~o( =∩ω∩= )m喵2", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        ctx.channel().eventLoop().execute(()->{
            try {
                Thread.sleep(20 * 1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端 ~o( =∩ω∩= )m喵3", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 用户自定义定时任务
        ctx.channel().eventLoop().schedule(()->{
            try {
                Thread.sleep(5 * 1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端 ~o( =∩ω∩= )m喵4", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 5, TimeUnit.MILLISECONDS);


//        Thread.sleep(10 * 1000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端 ~o( =∩ω∩= )m喵2", CharsetUtil.UTF_8));

        System.out.println("go on...");

//        System.out.println("服务器读取到的线程是: "+Thread.currentThread().getName());
//        System.out.println("server ctx = " + ctx);
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = channel.pipeline();
//        System.out.println("channel 和  pipeline" + channel + "\n" + pipeline);
//        //将msg转成ByteBuffer
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送的消息是: "+ buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端的IP地址是: "+ ctx.channel().remoteAddress());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush是 write + flush
        // 将数据写入缓存，并刷新
        // 一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端 ~o( =∩ω∩= )m喵1", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
