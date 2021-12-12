package com.gky.io.netty.upload;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

import java.util.Iterator;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-22 19:15
 **/
public class HttpUploadClientHandler extends SimpleChannelInboundHandler<HttpObject> {
    private boolean readingChunks;

    public void messageReceived(ChannelHandlerContext ctx, HttpObject msg) {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse)msg;
            System.err.println("STATUS: " + response.status());
            System.err.println("VERSION: " + response.protocolVersion());
            if (!response.headers().isEmpty()) {
                Iterator var5 = response.headers().names().iterator();

                while(var5.hasNext()) {
                    CharSequence name = (CharSequence)var5.next();
                    Iterator var7 = response.headers().getAll(name).iterator();

                    while(var7.hasNext()) {
                        CharSequence value = (CharSequence)var7.next();
                        System.err.println("HEADER: " + name + " = " + value);
                    }
                }
            }

            if (response.status().code() == 200 && HttpHeaderUtil.isTransferEncodingChunked(response)) {
                this.readingChunks = true;
                System.err.println("CHUNKED CONTENT {");
            } else {
                System.err.println("CONTENT {");
            }
        }

        if (msg instanceof HttpContent) {
            HttpContent chunk = (HttpContent)msg;
            System.err.println(chunk.content().toString(CharsetUtil.UTF_8));
            if (chunk instanceof LastHttpContent) {
                if (this.readingChunks) {
                    System.err.println("} END OF CHUNKED CONTENT");
                } else {
                    System.err.println("} END OF CONTENT");
                }

                this.readingChunks = false;
            } else {
                System.err.println(chunk.content().toString(CharsetUtil.UTF_8));
            }
        }

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.channel().close();
    }
}
