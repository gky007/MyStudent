package com.gky.io.netty.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-22 19:47
 **/
public class GzipUtils {
    public GzipUtils() {
    }

    public static byte[] gzip(byte[] data) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(bos);
        gzip.write(data);
        gzip.finish();
        gzip.close();
        byte[] ret = bos.toByteArray();
        bos.close();
        return ret;
    }

    public static byte[] ungzip(byte[] data) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        GZIPInputStream gzip = new GZIPInputStream(bis);
        byte[] buf = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int num;
        while((num = gzip.read(buf, 0, buf.length)) != -1) {
            bos.write(buf, 0, num);
        }

        gzip.close();
        bis.close();
        byte[] ret = bos.toByteArray();
        bos.flush();
        bos.close();
        return ret;
    }

    public static void main(String[] args) throws Exception {
        String readPath = System.getProperty("user.dir") + File.separatorChar + "sources" + File.separatorChar + "006.jpg";
        File file = new File(readPath);
        FileInputStream in = new FileInputStream(file);
        byte[] data = new byte[in.available()];
        in.read(data);
        in.close();
        System.out.println("文件原始大小:" + data.length);
        byte[] ret1 = gzip(data);
        System.out.println("压缩之后大小:" + ret1.length);
        byte[] ret2 = ungzip(ret1);
        System.out.println("还原之后大小:" + ret2.length);
        String writePath = System.getProperty("user.dir") + File.separatorChar + "receive" + File.separatorChar + "006.jpg";
        FileOutputStream fos = new FileOutputStream(writePath);
        fos.write(ret2);
        fos.close();
    }
}

