package com.nettyDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerHelloWorld {
    // 监听线程组
    private EventLoopGroup bosssGroup;

    // 处理线程组
    private EventLoopGroup workGroup;

    private ServerBootstrap bootstrap;

    public ServerHelloWorld() {
        init();
    }


    private void init() {
        bosssGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();

        // 绑定线程组
        bootstrap.group(bosssGroup, workGroup);
        // 设置通道
        bootstrap.channel(NioServerSocketChannel.class);

        // 设置缓存
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_SNDBUF, 16 * 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                .option(ChannelOption.SO_KEEPALIVE, true);

    }

    // 监听连接
    public ChannelFuture doAccept(int port, final ChannelHandler channelHandlers) throws InterruptedException {

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(channelHandlers);
            }
        });

        ChannelFuture future = bootstrap.bind(port).sync();
        return future;
    }

    public void  release(){
        this.bosssGroup.shutdownGracefully();
        this.workGroup.shutdownGracefully();
    }

    public static void main(String[] args) {

        ServerHelloWorld server = null;
        ChannelFuture future = null;

        try{
            server =new ServerHelloWorld();
                future = server.doAccept(9999, new ServerHandler()); // ServerHandler 自定义处理逻辑
            System.out.println("server started in port: "+9999);

            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(future != null){
                try {
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
