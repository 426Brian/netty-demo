package com.nettyPro.protocolTcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

public class ServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        // 接收到数据并处理
        int len = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("服务器接收到数据如下：");
        System.out.println("数据长度：" + len);
        System.out.println("数据内容：" + new String(content, Charset.forName("utf-8")));

        System.out.println("服务器接收到消息包数量： " + (++this.count));

        // 服务器端回复消息
        String response = UUID.randomUUID().toString();
        byte[] bytes = response.getBytes(Charset.forName("utf-8"));
        int length = response.getBytes("utf-8").length;

        MessageProtocol msgRes = new MessageProtocol();
        msgRes.setContent(bytes);
        msgRes.setLen(length);
        ctx.writeAndFlush(msgRes);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
