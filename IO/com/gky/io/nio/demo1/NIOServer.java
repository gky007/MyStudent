package com.gky.io.nio.demo1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-23 13:59
 **/
public class NIOServer {
    public static void main(String[] args) throws IOException {
        //创建ServerSoketChannel网络通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到selector对象
        Selector selector = Selector.open();

        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel注册到selector关心事件是OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true) {
            //这里我们等待一秒，如果没有事件发生，就返回
            if (selector.select(1000) == 0) { //没事件发送
//                System.out.println("服务器等待了1秒，无连接");
                continue;
            }
            // 如果返回不是0,就获取到相关的selectionKey集合
            /**
             * 1.如果返回大于0，表示已经获得关注事件
             * 2.selector.selectedKeys() 返回关注事件的集合
             *   通过这个集合可以反向获得通道
             * */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 遍历SelectionKey集合
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                // 获取到selectionKey
                SelectionKey key = keyIterator.next();
                SocketChannel socketChannel = null;
                //根据key对应的通道发生的事件做响应的处理
                if (key.isAcceptable()) { // 如果是OP_ACCEPT，有新客户端
                    // 给该客户端生成一个socketChannel
                    socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个 socketChannel "+ socketChannel.getRemoteAddress());

                    // 将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    // 将socketChannel 注册到selector, 关注事件为OP_READ，同是给该socketChannel关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (key.isReadable()) { // 如果当前通道发生OP_READ事件
                    // 通过key 反向获取对应的channel
                    SocketChannel channel = (SocketChannel)key.channel();
                    // 获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    channel.read(buffer);
                    String response = new String(buffer.array());
                    System.out.println("from 客户端 " + response);
                }
                //从集合中移除selectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
