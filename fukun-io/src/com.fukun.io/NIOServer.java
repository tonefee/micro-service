package com.fukun.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * NIO通讯Server端
 *
 * @author tangyifei
 * @date 2019年7月16日19:33:09
 */
public class NIOServer {

    private static final int MAX_THREAD = 10;

    private static Selector selector;

    private static LinkedBlockingQueue<SelectionKey> requestQueue;

    private static ExecutorService threadPool;

    public static void main(String[] args) {
        init();
        listen();
    }

    private static void init() {
        ServerSocketChannel serverSocketChannel;

        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            // 将Channel设置为非阻塞的 NIO就是支持非阻塞的
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(9000), 100);
            // ServerSocket，就是负责去跟各个客户端连接请求的
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            // 就是仅仅关注这个ServerSocketChannel接收到的TCP连接的请求
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestQueue = new LinkedBlockingQueue<>(500);
        threadPool = Executors.newFixedThreadPool(10);
        for(int i = 0; i < MAX_THREAD; i++) {
            threadPool.submit(new Worker());
        }

    }

    private static void listen() {
        while (true) {
            try {
                selector.select();
                Iterator<SelectionKey> keysIterator = selector.selectedKeys().iterator();
                while (keysIterator.hasNext()) {
                    SelectionKey key = keysIterator.next();
                    // 可以认为一个SelectionKey是代表了一个请求
                    keysIterator.remove();
                    handleRequest(key);
                }

            } catch (Throwable t) {
                t.printStackTrace();
            }

        }

    }

    private static void handleRequest(SelectionKey key) throws IOException, ClosedChannelException {

        // 后台的线程池中的线程处理下面的代码逻辑
        SocketChannel channel = null;
        try {
            // 如果说这个Key是一个acceptable,也就是一个连接请求
            if (key.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                // 调用accept这个方法 就可以进行TCP三次握手了
                channel = serverSocketChannel.accept();
                // 握手成功的话就可以获取到一个TCP连接好的SocketChannel
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
                // 仅仅关注这个READ请求，就是人家发送数据过来的请求
            }

            // 如果说这个key是readable，是个发送了数据过来的话，此时需要读取客户端发送过来的数据
            else if (key.isReadable()) {
                channel = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int count = channel.read(buffer);
                // 通过底层的socket读取数据，写buffer中，position可能就会变成21之类的
                // 你读取到了多少个字节，此时buffer的position就会变成多少
                if (count > 0) {
                    // 准备读取刚写入的数据，就是将limit设置为当前position，将position设置为0，丢弃mark。一般就是先写入数据，接着准备从0开始读这段数据，就可以用flip
                    // position = 0，limit = 21，仅仅读取buffer中，0~21这段刚刚写入进去的数据
                    buffer.flip();
                    System.out.println("服务端接收请求：" + new String(buffer.array(), 0, count));
                    channel.register(selector, SelectionKey.OP_WRITE);
                }

            } else if (key.isWritable()) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.put("收到".getBytes());
                buffer.flip();
                channel = (SocketChannel) key.channel();
                channel.write(buffer);
                channel.register(selector, SelectionKey.OP_READ);
            }

        } catch (Throwable t) {
            t.printStackTrace();
            if (channel != null) {
                channel.close();
            }

        }

    }

    // 创建一个线程任务来执行

    static class Worker implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    SelectionKey key = requestQueue.take();
                    handleRequest(key);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

        private void handleRequest(SelectionKey key) throws IOException, ClosedChannelException {
            // 假设想象一下，后台有个线程池获取到了请求
            // 下面的代码，都是在后台线程池的工作线程里在处理和执行
            SocketChannel channel = null;
            try {
                // 如果说这个key是个acceptable，是个连接请求的话
                if (key.isAcceptable()) {
                    System.out.println("[" + Thread.currentThread().getName() + "]接收到连接请求");
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    // 调用accept方法 和客户端进行三次握手
                    channel = serverSocketChannel.accept();
                    System.out.println("[" + Thread.currentThread().getName() + "]建立连接时获取到的channel=" + channel);
                    // 如果三次握手成功了之后，就可以获取到一个建立好TCP连接的SocketChannel
                    // 这个SocketChannel大概可以理解为，底层有一个Socket，是跟客户端进行连接的
                    // 你的SocketChannel就是联通到那个Socket上去，负责进行网络数据的读写的
                    // 设置为非阻塞的
                    channel.configureBlocking(false);
                    // 关注的是Reade请求
                    channel.register(selector, SelectionKey.OP_READ);
                }
                // 如果说这个key是readable，是个发送了数据过来的话，此时需要读取客户端发送过来的数据
                else if (key.isReadable()) {
                    channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int count = channel.read(buffer);
                    // 通过底层的socket读取数据，写入buffer中，position可能就会变成21之类的
                    // 你读取到了多少个字节，此时buffer的position就会变成多少
                    System.out.println("[" + Thread.currentThread().getName() + "]接收到请求");
                    if (count > 0) {
                        buffer.flip();
                        // position = 0，limit = 21，仅仅读取buffer中，0~21这段刚刚写入进去的数据
                        System.out.println("服务端接收请求：" + new String(buffer.array(), 0, count));
                        channel.register(selector, SelectionKey.OP_WRITE);

                    }

                } else if (key.isWritable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    buffer.put("收到".getBytes());
                    buffer.flip();
                    channel = (SocketChannel) key.channel();
                    channel.write(buffer);
                    channel.register(selector, SelectionKey.OP_READ);
                }

            } catch (Throwable t) {
                t.printStackTrace();

                if (channel != null) {
                    channel.close();
                }


            }


        }


    }

}
