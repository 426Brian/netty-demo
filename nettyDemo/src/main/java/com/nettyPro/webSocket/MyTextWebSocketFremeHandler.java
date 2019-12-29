package com.nettyPro.webSocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

// 这里表示一个文本帧
public class MyTextWebSocketFremeHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到消息：" + msg.text());

        // 回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间： " + LocalDateTime.now()+" " + msg.text()));
    }

    /**
     * 客户端连接后就立马触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        /**
         *  id 唯一   id().asLongText()
         *  id 会重复  id().asShortText()
         */

        System.out.println("handlerAdded 被调用 " + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用 " + ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用 " + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("发生异常 " + cause.getMessage());
        // 关闭
        ctx.close();
    }
}
