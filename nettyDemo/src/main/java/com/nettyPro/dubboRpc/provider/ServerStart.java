package com.nettyPro.dubboRpc.provider;

import com.nettyPro.dubboRpc.netty.NettyServer;

public class ServerStart {
    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1", 7000);
    }
}
