package com.wy.rpc.test;

import com.wy.rpc.annotation.Service;
import com.wy.rpc.service.HelloObject;
import com.wy.rpc.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class HelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到消息：{}", object.getMessage());
        return "这是Impl1方法";
    }

}
