package com.gky.io.netty.upload;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.ServerCookieDecoder;
import io.netty.handler.codec.http.ServerCookieEncoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-22 19:17
 **/
public class HttpUploadServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    private static final Logger logger = Logger.getLogger(HttpUploadServerHandler.class.getName());
    private HttpRequest request;
    private boolean readingChunks;
    private final StringBuilder responseContent = new StringBuilder();
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(16384L);
    private HttpPostRequestDecoder decoder;

    static {
        DiskFileUpload.deleteOnExitTemporaryFile = true;
        DiskFileUpload.baseDirectory = "D:" + File.separatorChar + "aa";
//        DiskAttribute.deleteOnExitTemporaryFile = true;
//        DiskAttribute.baseDirectory = "D:" + File.separatorChar + "aa";
    }

    public HttpUploadServerHandler() {
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (this.decoder != null) {
            this.decoder.cleanFiles();
        }

    }

    public void messageReceived(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = this.request = (HttpRequest)msg;
            URI uri = new URI(request.uri());
            if (!uri.getPath().startsWith("/form")) {
                this.writeMenu(ctx);
                return;
            }

            this.responseContent.setLength(0);
            this.responseContent.append("WELCOME TO THE WILD WILD WEB SERVER\r\n");
            this.responseContent.append("===================================\r\n");
            this.responseContent.append("VERSION: " + request.protocolVersion().text() + "\r\n");
            this.responseContent.append("REQUEST_URI: " + request.uri() + "\r\n\r\n");
            this.responseContent.append("\r\n\r\n");
            Iterator var6 = request.headers().iterator();

            while(var6.hasNext()) {
                Map.Entry<CharSequence, CharSequence> entry = (Map.Entry)var6.next();
                this.responseContent.append("HEADER: " + entry.getKey() + '=' + entry.getValue() + "\r\n");
            }

            this.responseContent.append("\r\n\r\n");
            String value = (String)request.headers().getAndConvert(HttpHeaderNames.COOKIE);
            Set cookies;
            if (value == null) {
                cookies = Collections.emptySet();
            } else {
                cookies = ServerCookieDecoder.decode(value);
            }

            Iterator var8 = cookies.iterator();

            while(var8.hasNext()) {
                Cookie cookie = (Cookie)var8.next();
                this.responseContent.append("COOKIE: " + cookie + "\r\n");
            }

            this.responseContent.append("\r\n\r\n");
            QueryStringDecoder decoderQuery = new QueryStringDecoder(request.uri());
            Map<String, List<String>> uriAttributes = decoderQuery.parameters();
            Iterator var10 = uriAttributes.entrySet().iterator();

            while(var10.hasNext()) {
                Map.Entry<String, List<String>> attr = (Map.Entry)var10.next();
                Iterator var12 = ((List)attr.getValue()).iterator();

                while(var12.hasNext()) {
                    String attrVal = (String)var12.next();
                    this.responseContent.append("URI: " + (String)attr.getKey() + '=' + attrVal + "\r\n");
                }
            }

            this.responseContent.append("\r\n\r\n");
            if (request.method().equals(HttpMethod.GET)) {
                this.responseContent.append("\r\n\r\nEND OF GET CONTENT\r\n");
                return;
            }

            try {
                this.decoder = new HttpPostRequestDecoder(factory, request);
            } catch (HttpPostRequestDecoder.ErrorDataDecoderException var14) {
                var14.printStackTrace();
                this.responseContent.append(var14.getMessage());
                this.writeResponse(ctx.channel());
                ctx.channel().close();
                return;
            }

            this.readingChunks = HttpHeaderUtil.isTransferEncodingChunked(request);
            this.responseContent.append("Is Chunked: " + this.readingChunks + "\r\n");
            this.responseContent.append("IsMultipart: " + this.decoder.isMultipart() + "\r\n");
            if (this.readingChunks) {
                this.responseContent.append("Chunks: ");
                this.readingChunks = true;
            }
        }

        if (this.decoder != null) {
            if (msg instanceof HttpContent) {
                HttpContent chunk = (HttpContent)msg;

                try {
                    this.decoder.offer(chunk);
                } catch (HttpPostRequestDecoder.ErrorDataDecoderException var13) {
                    var13.printStackTrace();
                    this.responseContent.append(var13.getMessage());
                    this.writeResponse(ctx.channel());
                    ctx.channel().close();
                    return;
                }

                this.responseContent.append('o');
                this.readHttpDataChunkByChunk();
                if (chunk instanceof LastHttpContent) {
                    this.writeResponse(ctx.channel());
                    this.readingChunks = false;
                    this.reset();
                }
            }
        } else {
            this.writeResponse(ctx.channel());
        }

    }

    private void reset() {
        this.request = null;
        this.decoder.destroy();
        this.decoder = null;
    }

    private void readHttpDataChunkByChunk() throws Exception {
        while(true) {
            try {
                if (this.decoder.hasNext()) {
                    InterfaceHttpData data = this.decoder.next();
                    if (data == null) {
                        continue;
                    }

                    try {
                        this.writeHttpData(data);
                        continue;
                    } finally {
                        data.release();
                    }
                }
            } catch (HttpPostRequestDecoder.EndOfDataDecoderException var6) {
                this.responseContent.append("\r\n\r\nEND OF CONTENT CHUNK BY CHUNK\r\n\r\n");
            }

            return;
        }
    }

    private void writeHttpData(InterfaceHttpData data) throws Exception {
        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
            Attribute attribute = (Attribute)data;

            String value;
            try {
                value = attribute.getValue();
            } catch (IOException var5) {
                var5.printStackTrace();
                this.responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": " + attribute.getName() + " Error while reading value: " + var5.getMessage() + "\r\n");
                return;
            }

            if (value.length() > 100) {
                this.responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": " + attribute.getName() + " data too long\r\n");
            } else {
                this.responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": " + attribute + "\r\n");
            }
        } else {
            this.responseContent.append("\r\n -----------start-------------\r\n");
            this.responseContent.append("\r\nBODY FileUpload: " + data.getHttpDataType().name() + ": " + data + "\r\n");
            this.responseContent.append("\r\n ------------end------------\r\n");
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                FileUpload fileUpload = (FileUpload)data;
                if (fileUpload.isCompleted()) {
                    System.out.println("file name : " + fileUpload.getFilename());
                    System.out.println("file length: " + fileUpload.length());
                    System.out.println("file maxSize : " + fileUpload.getMaxSize());
                    System.out.println("file path :" + fileUpload.getFile().getPath());
                    System.out.println("file absolutepath :" + fileUpload.getFile().getAbsolutePath());
                    System.out.println("parent path :" + fileUpload.getFile().getParentFile());
                    if (fileUpload.length() < 10485760L) {
                        this.responseContent.append("\tContent of file\r\n");
                        this.responseContent.append("\r\n");
                    } else {
                        this.responseContent.append("\tFile too long to be printed out:" + fileUpload.length() + "\r\n");
                    }

                    fileUpload.renameTo(new File(fileUpload.getFile().getPath()));
                } else {
                    this.responseContent.append("\tFile to be continued but should not!\r\n");
                }
            }
        }

    }

    private void writeResponse(Channel channel) {
        ByteBuf buf = Unpooled.copiedBuffer(this.responseContent.toString(), CharsetUtil.UTF_8);
        this.responseContent.setLength(0);
        boolean close = this.request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true) || this.request.protocolVersion().equals(HttpVersion.HTTP_1_0) && !this.request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE, true);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        if (!close) {
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
        }

        String value = (String)this.request.headers().getAndConvert(HttpHeaderNames.COOKIE);
        Set cookies;
        if (value == null) {
            cookies = Collections.emptySet();
        } else {
            cookies = ServerCookieDecoder.decode(value);
        }

        if (!cookies.isEmpty()) {
            Iterator var8 = cookies.iterator();

            while(var8.hasNext()) {
                Cookie cookie = (Cookie)var8.next();
                response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.encode(cookie));
            }
        }

        ChannelFuture future = channel.writeAndFlush(response);
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

    }

    private void writeMenu(ChannelHandlerContext ctx) {
        this.responseContent.setLength(0);
        this.responseContent.append("<html>");
        this.responseContent.append("<head>");
        this.responseContent.append("<title>Netty Test Form</title>\r\n");
        this.responseContent.append("</head>\r\n");
        this.responseContent.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");
        this.responseContent.append("<table border=\"0\">");
        this.responseContent.append("<tr>");
        this.responseContent.append("<td>");
        this.responseContent.append("<h1>Netty Test Form</h1>");
        this.responseContent.append("Choose one FORM");
        this.responseContent.append("</td>");
        this.responseContent.append("</tr>");
        this.responseContent.append("</table>\r\n");
        this.responseContent.append("<CENTER>GET FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        this.responseContent.append("<FORM ACTION=\"/formget\" METHOD=\"GET\">");
        this.responseContent.append("<input type=hidden name=getform value=\"GET\">");
        this.responseContent.append("<table border=\"0\">");
        this.responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
        this.responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
        this.responseContent.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
        this.responseContent.append("</td></tr>");
        this.responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        this.responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        this.responseContent.append("</table></FORM>\r\n");
        this.responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        this.responseContent.append("<CENTER>POST FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        this.responseContent.append("<FORM ACTION=\"/formpost\" METHOD=\"POST\">");
        this.responseContent.append("<input type=hidden name=getform value=\"POST\">");
        this.responseContent.append("<table border=\"0\">");
        this.responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
        this.responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
        this.responseContent.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
        this.responseContent.append("<tr><td>Fill with file (only file name will be transmitted): <br> <input type=file name=\"myfile\">");
        this.responseContent.append("</td></tr>");
        this.responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        this.responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        this.responseContent.append("</table></FORM>\r\n");
        this.responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        this.responseContent.append("<CENTER>POST MULTIPART FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        this.responseContent.append("<FORM ACTION=\"/formpostmultipart\" ENCTYPE=\"multipart/form-data\" METHOD=\"POST\">");
        this.responseContent.append("<input type=hidden name=getform value=\"POST\">");
        this.responseContent.append("<table border=\"0\">");
        this.responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
        this.responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
        this.responseContent.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
        this.responseContent.append("<tr><td>Fill with file: <br> <input type=file name=\"myfile\">");
        this.responseContent.append("</td></tr>");
        this.responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        this.responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        this.responseContent.append("</table></FORM>\r\n");
        this.responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        this.responseContent.append("</body>");
        this.responseContent.append("</html>");
        ByteBuf buf = Unpooled.copiedBuffer(this.responseContent.toString(), CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
        ctx.channel().writeAndFlush(response);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.log(Level.WARNING, this.responseContent.toString(), cause);
        ctx.channel().close();
    }
}
