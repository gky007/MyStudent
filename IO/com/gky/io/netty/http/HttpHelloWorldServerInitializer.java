package com.gky.io.netty.http;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-22 19:21
 **/
public class HttpHelloWorldServerInitializer  extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    public HttpHelloWorldServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    public void initChannel(SocketChannel sc) {
        ChannelPipeline pipeline = sc.pipeline();
        if (this.sslCtx != null) {
            pipeline.addLast(new ChannelHandler[]{this.sslCtx.newHandler(sc.alloc())});
        }
        // 加入一个netty提供的编解码器 HttpServerCodec
        pipeline.addLast(new ChannelHandler[]{new HttpServerCodec()});
        pipeline.addLast(new ChannelHandler[]{new HttpHelloWorldServerHandler()});
    }
}