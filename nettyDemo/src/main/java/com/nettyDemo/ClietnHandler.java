package com.nettyDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ClietnHandler extends ChannelInboundHandlerAdapter {
    /**
     * 处理业务逻辑
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            ByteBuf readBuffer = (ByteBuf) msg;
            byte[] tmpDatas = new byte[readBuffer.readableBytes()];
            readBuffer.readBytes(tmpDatas);

            String message = new String(tmpDatas, "utf-8");
            System.out.println("message from server: "+message);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ReferenceCountUtil.release(msg);
        }


    }

    /**
     * 处理异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("client exceptionCaught method run ...");
        ctx.close();
    }
}
