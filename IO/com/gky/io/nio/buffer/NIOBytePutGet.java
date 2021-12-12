package com.gky.io.nio.buffer;

import java.nio.ByteBuffer;

/**
 * @version 1.0.0
 * @description: 顺序取出
 * @author: Mr.G
 * @date: 2021-10-23 09:33
 **/
public class NIOBytePutGet {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.putInt(1);
        buffer.putLong(2L);
        buffer.putChar('三');
        buffer.putShort((short)4);

        // 取出
        buffer.flip();

        System.out.println("buffer.getInt() " + buffer.getInt());
        System.out.println("buffer.getLong() " + buffer.getLong());
        System.out.println("buffer.getChar() " + buffer.getChar());
        System.out.println("buffer.getShort() " + buffer.getShort());


    }
}
