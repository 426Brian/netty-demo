package com.nettyDemo;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientHelloWorld {
    private EventLoopGroup group;

    private Bootstrap bootstrap;

    public ClientHelloWorld() {
        init();
    }

    private void init() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();

        // 绑定线程组
        bootstrap.group(group);
        // 设置通道
        bootstrap.channel(NioSocketChannel.class);

    }

    // 监听连接
    public ChannelFuture doRequest(String host, int port, final ChannelHandler channelHandlers) throws InterruptedException {

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(channelHandlers);
            }
        });

        ChannelFuture future = bootstrap.connect(host, port).sync();
        return future;
    }

    public void release() {
        this.group.shutdownGracefully();
    }

    public static void main(String[] args) {
        ClientHelloWorld client = null;
        ChannelFuture future = null;

        try {
            client = new ClientHelloWorld();
            future = client.doRequest("localhost", 9999, new ClietnHandler()); // ClietnHandler 自定义处理逻辑

            Scanner scanner = null;
            while (true) {
                scanner = new Scanner(System.in);
                System.out.println("enter message send to server (enter 'exit' to close client)");

                String line = scanner.nextLine();
                if ("exit".equals(line)) {
                    future.channel().writeAndFlush(Unpooled.copiedBuffer(line.getBytes("utf-8"))).addListener(ChannelFutureListener.CLOSE);
                    break;
                }
                future.channel().writeAndFlush(Unpooled.copiedBuffer(line.getBytes("utf-8")));
                TimeUnit.SECONDS.sleep(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (future != null) {
                try {
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
