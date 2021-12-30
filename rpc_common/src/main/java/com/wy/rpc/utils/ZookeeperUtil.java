package com.wy.rpc.utils;

import com.wy.rpc.enumeration.RpcError;
import com.wy.rpc.exception.RpcException;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author raving
 * 创建zk连接，注册服务
 *
 *
 */
public class ZookeeperUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperUtil.class);

    private static final Set<String> serviceNames = new HashSet<>();
    private static InetSocketAddress address;

    private static final String SERVER_ADDR = "47.103.146.210:2181";

    static {
        try {
            List<String> list = getConnection().getChildren("/",null);
            if(!list.contains("rpc")){
                getConnection().create("/rpc",null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                getConnection().create("/rpc/services",null,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
            logger.error("连接到Zookeeper时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    public static void registerService(String serviceName, InetSocketAddress address)   {
        try {
            String path = "/rpc/services/" + serviceName;
            List<String> list = getConnection().getChildren("/rpc/services",null);
            if(!list.contains(serviceName)){
                getConnection().create(path,null,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
            path += "/"+ address.getHostName()+":"+ address.getPort();
            getConnection().create(path, null,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
        ZookeeperUtil.address = address;
        serviceNames.add(serviceName);
    }

    public static List<String> getAllInstance(String serviceName) throws InterruptedException, KeeperException {
        String path = "/rpc/services/" + serviceName;
        return getConnection().getChildren(path,null);
    }

    private static ZooKeeper getConnection()  {
        try {
            return new ZooKeeper(SERVER_ADDR, 20000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            throw  new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    public static void clearRegistry() {
        if(!serviceNames.isEmpty() && address != null) {
            String host = address.getHostName();
            int port = address.getPort();
            for (String serviceName : serviceNames) {
                try {
                    String path = "/rpc/services/" + serviceName;
                    List<String> list = getConnection().getChildren(path, null);
                    list.forEach(ele -> {
                        String temPath = path + "/" + ele;
                        Stat stat = new Stat();
                        try {
                            getConnection().getData(temPath, null, stat);
                            getConnection().delete(temPath, stat.getCversion());
                        } catch (KeeperException | InterruptedException e) {
                            e.printStackTrace();
                            logger.error("注销服务 {} 失败", serviceName, e);
                        }
                    });
                    getConnection().delete(path, -1);
                } catch (InterruptedException | KeeperException e) {
                    e.printStackTrace();
                    logger.error("注销服务 {} 失败", serviceName, e);
                }
            }
        }
    }

}
