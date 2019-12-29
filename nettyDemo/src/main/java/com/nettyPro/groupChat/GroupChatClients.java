package com.nettyPro.groupChat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClients {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    // 初始化
    public GroupChatClients() {
        try {
            // 得到 Selector
            selector = Selector.open();

            // 连接
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));

            // 设置非阻塞
            socketChannel.configureBlocking(false);

            // 注册
            socketChannel.register(selector, SelectionKey.OP_READ);

            userName = socketChannel.getLocalAddress().toString().substring(1);

            System.out.println(userName + " is ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatClients groupChatClients = new GroupChatClients();

        // 循环读数据
        new Thread(() -> {
            while (true) {
                groupChatClients.read();
            }

        }).start();


        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String info = scanner.nextLine();
            groupChatClients.sendInfo(info);
        }

    }

    // 发送数据到服务器端
    public void sendInfo(String info) {
        info = userName + " 说: " + info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取服务器信息
    public void read() {
        int select = 0;
        try {
            select = selector.select();

            if (select > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);

                        socketChannel.read(buffer);

                        String msg = new String(buffer.array()).trim();
                        System.out.println(msg);
                    }

                    // 移除 key
                    iterator.remove();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
