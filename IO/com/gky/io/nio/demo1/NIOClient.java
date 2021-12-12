package com.gky.io.nio.demo1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-23 14:30
 **/
public class NIOClient {
    public static void main(String[] args) throws IOException {
        //得到一个socketchannel网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务的ip端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 6666);
        // 连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为需要时间连接，客户端做其他工作");
            }
        }
        // 如果连接成功就发送数据
        String str = "hello, world";
        // 根据str的字节大小创建buffer
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        // 发送数据,将buffer数据写入channel
        socketChannel.write(buffer);
        System.in.read();
    }
}
