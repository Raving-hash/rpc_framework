package com.wy.rpc.loadBalancer;

import java.util.List;

public class RoundRobinLoadBalancer implements LoadBalancer{
        private int index = 0;

    @Override
    public String select(List<String> instances) {
        if(index >= instances.size()){
            index = index % instances.size();
        }
        return instances.get(index++);
    }
}
