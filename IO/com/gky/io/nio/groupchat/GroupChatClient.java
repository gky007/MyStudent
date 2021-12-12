package com.gky.io.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-23 15:52
 **/
public class GroupChatClient {
    //定义的相关属性
    private final String HOST = "127.0.0.1"; // 服务器IP
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    public GroupChatClient() {
        try {
            this.selector = Selector.open();
            this.socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            //设置非阻塞
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            this.userName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(userName + " is OK......");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(String info) {
        try {
            info = userName + " 说: " + info;
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInfo() {
        // 当前可用的通道
        try {
            int readChannel = selector.select();
            if (readChannel > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        //得到相关的通道
                        SocketChannel channel = (SocketChannel)key.channel();
                        //的到一个buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg);
                    }
                }
                iterator.remove();
            } else {
                System.out.println("没有可用的通道......");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatClient chatClient = new GroupChatClient();
        new Thread(()->{
            chatClient.readInfo();
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        // 发送数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }
}
