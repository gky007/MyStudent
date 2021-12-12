package com.gky.io.nio.buffer;

import java.nio.ByteBuffer;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-23 09:38
 **/
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        for (int i = 0; i < 64; i++) {
            byteBuffer.put((byte) i);
        }

        byteBuffer.flip();

        //得到只读的buffer
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();

        while (readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.get());
        }

        readOnlyBuffer.put((byte) 64); //抛出异常ReadOnlyBufferException
    }
}
