package com.nettyPro.tcp;

import com.nettyPro.simple.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class NettyClient {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6668;

    public static void main(String[] args) {
        // 客户端需要一个时间循环组
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        // 创建客户端启动对象 Bootstrap
        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(eventLoopGroup)   // 设置线程组
                    .channel(NioSocketChannel.class) // 客户端设置通道实现类
                    .handler(new ClientInitializer());

            System.out.println("客户端 is OK");

            // 客户端连接服务器
            ChannelFuture channelFuture = bootstrap.connect(HOST, PORT).sync();

            // 关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            // 优雅关闭
            eventLoopGroup.shutdownGracefully();
        }


    }

}
