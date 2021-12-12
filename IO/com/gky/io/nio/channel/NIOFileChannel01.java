package com.gky.io.nio.channel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-23 00:08
 **/
public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String message = "hello, world!单生g";
        String path = System.getProperty("user.dir")+ File.separator + "IO"+ File.separator + "sources" + File.separator + "test.txt";
        // 创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        // 通过输出流获取对应的文件channel
        // 这个fileChannel真实类型是FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();
        // 创建一个ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将message放入到byteBuffer
        byteBuffer.put(message.getBytes());
        //对betyBuffer进行翻转
        byteBuffer.flip();
        // 将byteBuffer写入到fileChannel
        fileChannel.write(byteBuffer);
        //关闭流
        fileOutputStream.close();

    }
}
