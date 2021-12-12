package com.gky.io.nio.buffer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @version 1.0.0
 * @description:
 *  scattering: 将数据写入到buffer时，可采取buffer数组，依次写入【分散】
 *  gathering: 从buffer读取数据时，可采取buffer数组，依次读
 *
 * @author: Mr.G
 * @date: 2021-10-23 09:59
 **/
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception {

        // 使用 serverSocketChannel 和 socketChannel 网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8000);

        // 绑定端口到socket,并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        System.out.println("inetSocketAddress.getAddress().getHostAddress() = " + inetSocketAddress.getAddress());
        // 创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        System.out.println("等待客户端连接......");
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.out.println("客户端 " + socketChannel.getLocalAddress()+" 已连接");
        int messageLength = 8; // 从客户端接受8个字节
        // 循环读取
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long rl = socketChannel.read(byteBuffers);
                byteRead += rl; //累积读取的字节数
                System.out.println("byteRead = " + byteRead);
                // 使用流打印，看看当前的buffer的position和limit
                Arrays.asList(byteBuffers).stream().map(buffer -> "当前: positon = " + buffer.position() + " , limit = " + buffer.limit()).forEach(System.out::println);
                //将所有的buffer进行反转filp
                Arrays.asList(byteBuffers).stream().forEach(buffer -> buffer.flip());
                //将数据读出显示到客户端
                long betyWrite = 0;
                while (betyWrite < messageLength) {
                    long wl = socketChannel.write(byteBuffers);
                    betyWrite += wl;
                }

                // 将所有的buffer 进行clear
                Arrays.asList(byteBuffers).stream().forEach(buffer -> {
                    buffer.clear();
                });
            }

        }
    }
}
