package com.wy.rpc.test;

import com.wy.rpc.annotation.Service;
import com.wy.rpc.service.ByeService;

@Service
public class ByeServiceImpl implements ByeService {

    @Override
    public String bye(String name) {
        return "bye, " + name;
    }
}