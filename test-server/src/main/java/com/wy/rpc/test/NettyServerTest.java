package com.wy.rpc.test;

import com.wy.rpc.annotation.ServiceScan;
import com.wy.rpc.serializer.CommonSerializer;
import com.wy.rpc.transport.RpcServer;
import com.wy.rpc.transport.netty.server.NettyServer;

@ServiceScan
public class NettyServerTest {

    public static void main(String[] args) {
        RpcServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.KRYO_SERIALIZER);
        server.start();
    }

}