package com.wy.rpc.registry;

import com.wy.rpc.enumeration.RpcError;
import com.wy.rpc.exception.RpcException;
import com.wy.rpc.loadBalancer.LoadBalancer;
import com.wy.rpc.loadBalancer.RoundRobinLoadBalancer;
import com.wy.rpc.utils.ZookeeperUtil;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

public class ZookeeperServiceDiscovery implements ServiceDiscovery{
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperUtil.class);

    private final LoadBalancer loadBalancer;

    public ZookeeperServiceDiscovery(LoadBalancer loadBanlancer){
        if (loadBanlancer == null){
            this.loadBalancer = new RoundRobinLoadBalancer();
        }else {
            this.loadBalancer = loadBanlancer;
        }
    }
    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<String> list = ZookeeperUtil.getAllInstance(serviceName);
            if(list.size() == 0) {
                logger.error("找不到对应的服务: " + serviceName);
                throw new RpcException(RpcError.SERVICE_NOT_FOUND);
            }
            String inet = loadBalancer.select(list);
            return new InetSocketAddress(inet.substring(0,inet.indexOf(":")),
                    Integer.parseInt(inet.substring(inet.indexOf(":")+1)));
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
        return null;
    }
}
