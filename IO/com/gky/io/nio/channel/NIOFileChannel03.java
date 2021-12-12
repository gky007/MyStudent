package com.gky.io.nio.channel;

import java.io.File;
import java.io.FileInputStream;
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
public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir")+ File.separator + "IO"+ File.separator + "sources" + File.separator + "test.txt";
        String path1 = System.getProperty("user.dir")+ File.separator + "IO"+ File.separator + "sources" + File.separator + "test1.txt";

        FileInputStream fileInputStream = new FileInputStream(path);
        FileChannel fileChannel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream(path1);
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while (true) {
            // 这里有一个重要操作，复位
            byteBuffer.clear();
            int read = fileChannel01.read(byteBuffer);
            System.out.println("read = " + read);
            if (read == -1) {
                break;
            }
            // 将buffer中的数据写入到 fileChannel02 --02.txt
            byteBuffer.flip();
            fileChannel02.write(byteBuffer);
        }

        // 关闭相关的流
        fileInputStream.close();
        fileOutputStream.close();
    }
}
