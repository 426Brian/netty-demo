package com.nettyPro.webSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            // 基于 http 协议， 使用 http 的 编码器 和 解码器
                            pipeline.addLast(new HttpServerCodec());

                            // 以块方式写， 添加 ChunkedWriteHandler
                            pipeline.addLast(new ChunkedWriteHandler());

                            /**
                             * http 数据在传输过程中是中断的，HttpObjectAggregator 将
                             * 多个段聚合起来， 浏览器发送大量数据时，会发出多次 http 请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            /**
                             * WebSocket 数据以帧的形式传递
                             * 浏览器请求 uri ws://localhost:7000/hello
                             *  WebSocketServerProtocolHandler 核心功能将 http 协议升级为 websocket 协议
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            // 处理业务 自定义 handler
                            pipeline.addLast(new MyTextWebSocketFremeHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}