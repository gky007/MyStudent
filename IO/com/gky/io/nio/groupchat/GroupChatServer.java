package com.gky.io.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-23 15:15
 **/
public class GroupChatServer {
    // 定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public GroupChatServer() {
        try{
            this.selector = Selector.open();
            this.listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //监听
    public void listen() {
        try {
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        //取出selectionKey
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress() + " 上线");
                        }
                        if (key.isReadable()) { // 通道发生read事件。即通道是可读状态
                            readData(key);
                        }
                        //当前的key删除，防止重复操作
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待......");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    //读取客户消息
    public void readData(SelectionKey key){
        // 定义一个socketChannel
        SocketChannel channel = null;
        try {
            //取到关联的channel
            channel = (SocketChannel)key.channel();
            channel.configureBlocking(false);
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            //读取通道数据到buffer
            int count = channel.read(byteBuffer);
            if (count > 0) {
                String msg = new String(byteBuffer.array());
                System.out.println("form 客户端 " + msg);
                // 向其他客户端转发消息
                sendInfoOtherClients(channel, msg);
            }
        } catch (Exception e) {
            try {
                System.out.println(channel.getRemoteAddress() + " 离线了...");
                key.channel();
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    private void sendInfoOtherClients(SocketChannel self, String msg) throws IOException {
        System.out.println("服务器转发消息中");
        for (SelectionKey key: selector.keys()) {
            SelectableChannel targetChannel = key.channel();
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                SocketChannel dest = (SocketChannel)targetChannel;
                dest.configureBlocking(false);
                //将msg，转储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
