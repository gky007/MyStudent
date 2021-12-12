package com.gky.io.netty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-22 19:22
 **/
public class HttpHelloWorldServerHandler extends ChannelHandlerAdapter {
    public HttpHelloWorldServerHandler() {
    }

    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws URISyntaxException {
        if (msg instanceof HttpRequest) {
            System.out.println("ctx.pipeline().hashCode()  = " + ctx.pipeline().hashCode() );
            System.out.println("HttpHelloWorldServerHandler.hashCode()  =  "+this.hashCode());

            System.out.println("msg 类型 "+msg.getClass());
            System.out.println( "客户端IP "+ ctx.channel().remoteAddress());

            HttpRequest req = (HttpRequest)msg;

            URI url = new URI(req.uri());
            if ("/favicon.ico".equals(url.getPath())) {
                System.out.println("请求了favicon.ico， 不做响应");
                return;
            }
            if (HttpHeaderUtil.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
            }

            boolean keepAlive = HttpHeaderUtil.isKeepAlive(req);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer("hello, 我是服务器".getBytes()));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.write(response);
            }
        }

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
