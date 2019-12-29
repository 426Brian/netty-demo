package com.nettyPro.protocolTcp;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 编码器
        pipeline.addLast(new MyMessageEncoder());

        //解码器
        pipeline.addLast(new MyMessageDecoder());

        pipeline.addLast(new ClientHandler());

    }
}
