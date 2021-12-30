package com.wy.rpc.test;

import com.wy.rpc.serializer.CommonSerializer;
import com.wy.rpc.service.ByeService;
import com.wy.rpc.service.HelloObject;
import com.wy.rpc.service.HelloService;
import com.wy.rpc.transport.RpcClient;
import com.wy.rpc.transport.RpcClientProxy;
import com.wy.rpc.transport.netty.client.NettyClient;

public class NettyClientTest {

    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.KRYO_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
        ByeService byeService = rpcClientProxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("Netty"));
    }
}
