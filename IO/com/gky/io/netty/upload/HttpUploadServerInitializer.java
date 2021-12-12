package com.gky.io.netty.upload;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-22 19:13
 **/
public class HttpUploadServerInitializer  extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    public HttpUploadServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        if (this.sslCtx != null) {
            pipeline.addLast("ssl", this.sslCtx.newHandler(ch.alloc()));
        }

        pipeline.addLast("codec", new HttpClientCodec());
        pipeline.addLast("inflater", new HttpContentDecompressor());
        pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
        pipeline.addLast("handler", new HttpUploadClientHandler());
    }
}
