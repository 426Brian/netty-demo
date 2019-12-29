package com.nettyPro.dubboRpc.customer;

import com.nettyPro.dubboRpc.netty.NettyClient;
import com.nettyPro.dubboRpc.publicInterface.HelloService;

public class ClientStart {
    public static final String PROVIDER_NAME = "HelloService#hello#";

    public static void main(String[] args) {
        NettyClient customer = new NettyClient();

        // 创建代理对象
        HelloService helloService = (HelloService) customer.getBean(HelloService.class, PROVIDER_NAME);

        String res = helloService.hello("hello rpc ***");

        System.out.println("调用的结果 = "+res);
    }
}
