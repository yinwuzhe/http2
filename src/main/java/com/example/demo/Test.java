package com.example.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.AsciiString;
import java.nio.charset.StandardCharsets;

public class Test {

    public static void main(String[] args) {
//        encodeInteger();
        getHuffmancode("hello");
    }

    private static void encodeInteger() {
        HpackHuffmanEncoder encoder = new HpackHuffmanEncoder();
        ByteBuf byteBuf = Unpooled.buffer();
        encodeInteger(byteBuf,0x20,5,1337);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        // 打印编码后的字节数组
        // 读取所有字节
        byteBuf.readBytes(bytes);
// 打印字节数组R
//        System.out.println(Arrays.toString(bytes));
        System.out.println("bytes.length = " + bytes.length);
        for (byte b : bytes) {
            System.out.print(String.format("%02X ", b));
        }
        System.out.println();
    }



    private static void encodeInteger(ByteBuf out, int mask, int n, long i) {
        assert n >= 0 && n <= 8 : "N: " + n;
        int nbits = 0xFF >>> (8 - n);
        if (i < nbits) {
            out.writeByte((int) (mask | i));
        } else {
            out.writeByte(mask | nbits);
            long length = i - nbits;
            for (; (length & ~0x7F) != 0; length >>>= 7) {
                out.writeByte((int) ((length & 0x7F) | 0x80));
            }
            out.writeByte((int) length);
        }
    }


    private static void getHuffmancode(String str) {
        HpackHuffmanEncoder encoder = new HpackHuffmanEncoder();
        ByteBuf byteBuf = Unpooled.buffer();
        // 编码字符串
        encoder.encode(byteBuf,str);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        // 打印编码后的字节数组
        // 读取所有字节
        byteBuf.readBytes(bytes);
// 打印字节数组
//        System.out.println(Arrays.toString(bytes));
        System.out.println("bytes.length = " + bytes.length);
        for (byte b : bytes) {
            System.out.print(String.format("%02X ", b));
        }
        System.out.println();
    }
}
