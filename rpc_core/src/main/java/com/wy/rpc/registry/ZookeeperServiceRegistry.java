package com.wy.rpc.registry;

import com.wy.rpc.enumeration.RpcError;
import com.wy.rpc.exception.RpcException;
import com.wy.rpc.utils.ZookeeperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class ZookeeperServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperUtil.class);

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            ZookeeperUtil.registerService(serviceName,inetSocketAddress);
        }catch (Exception e){
            logger.error("注册服务时有错误发生:", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }
}
