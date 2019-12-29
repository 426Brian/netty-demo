package com.nettyPro.groupChat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {
    private static final int PORT = 6667;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;


    // 初始化
    public GroupChatServer() {
        try {

            // 得到 Selector
            selector = Selector.open();

            // 得到 ServerSocketChannel
            serverSocketChannel = ServerSocketChannel.open();

            // 绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));

            // 设置非阻塞
            serverSocketChannel.configureBlocking(false);
            // 注册
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }

    // 监听客户端连接
    public void listen() {
        int select = 0;
        try {
            while (true) {
                select = selector.select();
                if (select > 0) {
                    // 有事件
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            System.out.println("client " + socketChannel.getRemoteAddress() + " 已上线");

                            // 设置非阻塞
                            socketChannel.configureBlocking(false);

                            // 注册
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        }

                        if (key.isReadable()) {
                            // 处理 读 操作
                            read(key);
                        }

                        // 移除 key
                        iterator.remove();

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // 处理 读 操作
    private void read(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            int read = socketChannel.read(buffer);
            if (read > 0) {
                String msg = new String(buffer.array()).trim();

                System.out.println("from client " + msg);
                // 转发到其他客户端
                sendToOtherClients(msg, socketChannel);
            }
        } catch (IOException e) {
            try {
                System.out.println("client " + socketChannel.getRemoteAddress() + " 已下线");
                // 取消key
                key.cancel();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }


    }

    // 转发到其他客户端
    private void sendToOtherClients(String msg, SocketChannel targetChannel) {
        Set<SelectionKey> keys = selector.keys();

        for (SelectionKey key : keys) {
            Channel channel = key.channel();
            if (channel instanceof SocketChannel && channel != targetChannel) {
                // 排除自己
                SocketChannel destChannel = (SocketChannel) channel;

                try {
                    destChannel.write(ByteBuffer.wrap(msg.getBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
