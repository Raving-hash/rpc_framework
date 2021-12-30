package com.wy.rpc.transport.netty.client;

import com.wy.rpc.entity.RpcResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class UnprocessedRequests {
    private static ConcurrentHashMap<String, CompletableFuture<RpcResponse>> unprocessedResponseFutures =new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<RpcResponse> future){
        unprocessedResponseFutures.put(requestId,future);
    }

    public void remove(String requestId) {
        unprocessedResponseFutures.remove(requestId);
    }

    public void complete(RpcResponse rpcResponse){
        CompletableFuture<RpcResponse> future = unprocessedResponseFutures.remove(rpcResponse.getRequestId());
        if(future != null){
            future.complete(rpcResponse);
        }else {
            throw new IllegalStateException();
        }
    }

}
