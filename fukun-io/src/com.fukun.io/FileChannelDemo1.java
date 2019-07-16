package com.fukun.io;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通过NIO进行文件的读写操作
 *
 * @author tangyifei
 * @date 2019年7月16日18:35:07
 */
public class FileChannelDemo1 {

    public static void main(String[] args) throws Exception {
        long begin = System.currentTimeMillis();
        // 构造一个传统的文件输出流
        FileOutputStream out = new FileOutputStream("F:\\number.txt");
        // 通过文件输出流获取到对应的FileChannel，以NIO的方式来写文件
        FileChannel channel = out.getChannel();
        // 将数据写入到Buffer中
        ByteBuffer buffer = ByteBuffer.wrap("tangfukun".getBytes());
        // 通过FileChannel管道将Buffer中的数据写到输出流中去，持久化到磁盘中去
        channel.write(buffer);
        channel.close();
        out.close();
        System.out.println("使用NIO实现文件的写所消耗的时间：" + String.valueOf(System.currentTimeMillis() - begin) + "毫秒");
    }

}
