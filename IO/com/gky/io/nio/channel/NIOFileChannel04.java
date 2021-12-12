package com.gky.io.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @version 1.0.0
 * @description: 使用transferForm完成0拷贝
 * @author: Mr.G
 * @date: 2021-10-23 00:08
 **/
public class NIOFileChannel04 {
    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir")+ File.separator + "IO"+ File.separator + "sources" + File.separator + "001.jpg";
        String path1 = System.getProperty("user.dir")+ File.separator + "IO"+ File.separator + "sources" + File.separator + "1.jpg";

        FileInputStream fileInputStream = new FileInputStream(path);
        FileChannel sources = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream(path1);
        FileChannel disch = fileOutputStream.getChannel();

        // 使用transferForm完成 0拷贝 拷贝
//        disch.transferFrom(sources,0,sources.size());
        sources.transferTo(0,sources.size(), disch);
        // 关闭相关的流
        fileInputStream.close();
        fileOutputStream.close();
    }
}
