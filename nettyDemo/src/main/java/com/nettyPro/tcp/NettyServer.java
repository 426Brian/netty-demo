package com.nettyPro.tcp;

import com.nettyPro.simple.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args)  {
        // 创建 bossGroup 和  workGroup
        /**
         * bossGroup  只处理连接请求
         * workGroup  客户端业务处理
         * 两个都是无限循环
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端启动对象, 配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class)  // 使用 NioServerSocketChannel作为服务器端的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持连接活动状态
                    .childHandler(new ServerInitializer());  // 给 workGroup 的 EventLoop 对应的管道设置处理器

            System.out.println("…… 服务器 is ready …… ");

            // 绑定端口启动服务器并且同步, 生成一个 ChannelFuture 对象
            ChannelFuture channelFuture = bootstrap.bind(6668).sync();

            // 关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 优雅关闭
            bossGroup.shutdownGracefully();
        }

    }
}
