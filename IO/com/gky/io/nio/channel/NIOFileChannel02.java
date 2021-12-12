package com.gky.io.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-23 00:08
 **/
public class NIOFileChannel02 {
    public static void main(String[] args) throws IOException {

        String path = System.getProperty("user.dir")+ File.separator + "IO"+ File.separator + "sources" + File.separator + "test.txt";
        File file = new File(path);
        // 创建文件输入流
        FileInputStream fileInputStream = new FileInputStream(file);
        // 通过输入流获取对应的文件channel
        // 这个fileChannel真实类型是FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();
        // 创建一个ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());
        // 将通道数据写入到Buffer
        fileChannel.read(byteBuffer);
        System.out.println("byteBuffer = " + new String(byteBuffer.array()));
        //关闭流
        fileInputStream.close();

    }
}
