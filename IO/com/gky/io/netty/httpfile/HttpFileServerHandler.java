package com.gky.io.netty.httpfile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-22 19:40
 **/
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String url;
    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");
    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    public HttpFileServerHandler(String url) {
        this.url = url;
    }

    public void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!request.decoderResult().isSuccess()) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
        } else if (request.method() != HttpMethod.GET) {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
        } else {
            String uri = request.uri();
            String path = this.sanitizeUri(uri);
            if (path == null) {
                sendError(ctx, HttpResponseStatus.FORBIDDEN);
            } else {
                File file = new File(path);
                if (!file.isHidden() && file.exists()) {
                    if (file.isDirectory()) {
                        if (uri.endsWith("/")) {
                            sendListing(ctx, file);
                        } else {
                            sendRedirect(ctx, uri + '/');
                        }

                    } else if (!file.isFile()) {
                        sendError(ctx, HttpResponseStatus.FORBIDDEN);
                    } else {
                        RandomAccessFile randomAccessFile = null;

                        try {
                            randomAccessFile = new RandomAccessFile(file, "r");
                        } catch (FileNotFoundException var12) {
                            sendError(ctx, HttpResponseStatus.NOT_FOUND);
                            return;
                        }

                        long fileLength = randomAccessFile.length();
                        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                        HttpHeaderUtil.setContentLength(response, fileLength);
                        setContentTypeHeader(response, file);
                        if (HttpHeaderUtil.isKeepAlive(request)) {
                            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                        }

                        ctx.write(response);
                        // 写出ChunkedFile
                        ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile, 0L, fileLength, 8192), ctx.newProgressivePromise());
                        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
                            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                                if (total < 0L) {
                                    System.err.println("Transfer progress: " + progress);
                                } else {
                                    System.err.println("Transfer progress: " + progress + " / " + total);
                                }

                            }

                            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                                System.out.println("Transfer complete.");
                            }
                        });
                        // 空包，知道发送的最后一个请求
                        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
                        if (!HttpHeaderUtil.isKeepAlive(request)) {
                            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
                        }

                    }
                } else {
                    sendError(ctx, HttpResponseStatus.NOT_FOUND);
                }
            }
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ctx.channel().isActive()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            ctx.close();
        }

    }

    private String sanitizeUri(String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException var4) {
                throw new Error();
            }
        }

        if (!uri.startsWith(this.url)) {
            return null;
        } else if (!uri.startsWith("/")) {
            return null;
        } else {
            uri = uri.replace('/', File.separatorChar);
            return !uri.contains(File.separator + '.') && !uri.contains('.' + File.separator) && !uri.startsWith(".") && !uri.endsWith(".") && !INSECURE_URI.matcher(uri).matches() ? System.getProperty("user.dir") + File.separator + uri : null;
        }
    }

    private static void sendListing(ChannelHandlerContext ctx, File dir) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        StringBuilder ret = new StringBuilder();
        String dirPath = dir.getPath();
        ret.append("<!DOCTYPE html>\r\n");
        ret.append("<html><head><title>");
        ret.append(dirPath);
        ret.append(" 目录：");
        ret.append("</title></head><body>\r\n");
        ret.append("<h3>");
        ret.append(dirPath).append(" 目录：");
        ret.append("</h3>\r\n");
        ret.append("<ul>");
        ret.append("<li>链接：<a href=\"../\">..</a></li>\r\n");
        File[] var8;
        int var7 = (var8 = dir.listFiles()).length;

        for(int var6 = 0; var6 < var7; ++var6) {
            File f = var8[var6];
            if (!f.isHidden() && f.canRead()) {
                String name = f.getName();
                if (ALLOWED_FILE_NAME.matcher(name).matches()) {
                    ret.append("<li>链接：<a href=\"");
                    ret.append(name);
                    ret.append("\">");
                    ret.append(name);
                    ret.append("</a></li>\r\n");
                }
            }
        }

        ret.append("</ul></body></html>\r\n");
        ByteBuf buffer = Unpooled.copiedBuffer(ret, CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaderNames.LOCATION, newUri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
    }
}
