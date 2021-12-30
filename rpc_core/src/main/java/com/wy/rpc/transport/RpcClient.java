package com.wy.rpc.transport;

import com.wy.rpc.entity.RpcRequest;
import com.wy.rpc.serializer.CommonSerializer;

public interface RpcClient {
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    Object sendRequest(RpcRequest rpcRequest);
}
