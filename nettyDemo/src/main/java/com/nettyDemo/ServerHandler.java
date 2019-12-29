package com.nettyDemo;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.nio.ByteBuffer;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 处理业务逻辑
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf readBuffer = (ByteBuf) msg;
        byte[] tmpDatas = new byte[readBuffer.readableBytes()];
        readBuffer.readBytes(tmpDatas);

        String message = new String(tmpDatas, "utf-8");
        System.out.println("message from client: "+message);
        if("exit".equals(message)){
            ctx.close();
            return;
        }

        String line = "server message to client";
        ctx.writeAndFlush(Unpooled.copiedBuffer(line.getBytes()));
    }

    /**
     * 处理异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("server exceptionCaught method run ...");
        ctx.close();
    }


}
