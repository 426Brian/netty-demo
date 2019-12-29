package com.nettyPro.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

public class ServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);

        // buffer 转成字符串
        String message = new String(buffer, 0, buffer.length);

        System.out.println("服务器接收到数据： " + message);
        int num = ++this.count;
        System.out.println("服务器接收到消息量： " +(++num));

        // 服务器会送数据给客户端
        ctx.writeAndFlush(Unpooled.copiedBuffer(UUID.randomUUID().toString()+"["+num+"] ", Charset.forName("utf-8")));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
