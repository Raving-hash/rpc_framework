package com.wy.rpc.loadBalancer;

import java.util.List;

public interface LoadBalancer {
    String select(List<String> instances);
}
