package com.gky.io.nio.buffer;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @version 1.0.0
 * @description: 1.MappedByteBuffer 可以让文件直接在堆外内存修改
 * @author: Mr.G
 * @date: 2021-10-23 09:44
 **/
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception {
        String path = System.getProperty("user.dir")+ File.separator + "IO"+ File.separator + "sources" + File.separator + "test.txt";

        RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw");

        FileChannel fileChannel = randomAccessFile.getChannel();
        /**
         * 参数1：使用读写模式
         * 参数2:  0 可以直接修改的起始位置
         * 餐胡3： 5：是映射到内存的大小，即将test.txt的多少个字节映射到内存中
         *         可以直接修改的范围是0-5
         * */
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');

        randomAccessFile.close();
        System.out.println("修改成功");

    }
}
