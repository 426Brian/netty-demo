package com.nettyPro.protocolTcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class ClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 使用客户端发送 10 条数据 hello, server
        for (int i = 0; i < 10; i++) {
            String msg = "今天天气冷，吃火锅";
            byte[] content = msg.getBytes(Charset.forName("utf-8"));
            int length = content.length;

            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setContent(content);
            messageProtocol.setLen(length);

            ctx.writeAndFlush(messageProtocol);
        }


    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        // 接收到数据并处理
        int len = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("客户端接收到数据如下：");
        System.out.println("客户端数据长度：" + len);
        System.out.println("客户端数据内容：" + new String(content, Charset.forName("utf-8")));

        System.out.println("客户端接收到消息包数量： " + (++this.count));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

