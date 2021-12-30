package com.wy.rpc.transport;

import com.wy.rpc.entity.RpcRequest;
import com.wy.rpc.entity.RpcResponse;
import com.wy.rpc.transport.netty.client.NettyClient;
import com.wy.rpc.utils.RpcMessageChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RpcClientProxy implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);

    private final RpcClient client;

    public RpcClientProxy(RpcClient client){
        this.client = client;
    }

    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(),method.getDeclaringClass().getName(),
                method.getName(),args,method.getParameterTypes(),false);
        RpcResponse rpcResponse = null;
        if(client instanceof NettyClient){
            try {
                CompletableFuture<RpcResponse> future = (CompletableFuture<RpcResponse>) client.sendRequest(rpcRequest);
                rpcResponse = future.get();
            }catch (Exception e){
                logger.error("方法调用请求发送失败", e);
                return null;
            }
        }
        RpcMessageChecker.check(rpcRequest, rpcResponse);
        return rpcResponse.getData();
    }
}
