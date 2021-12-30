package com.wy.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author raving
 * 序列化ID，暂时只提供KRYO一种算法
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {
    KRYO(0);

    private int code;
}
