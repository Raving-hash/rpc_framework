package com.wy.rpc.factory;

import java.util.HashMap;

/**
 * 单例工厂，保存创建过的object
 */
public class SingletonFactory {
    private static HashMap<Class, Object> objectMap = new HashMap<>();

    private SingletonFactory(){}

    public static <T> T getInstance(Class<T> clazz){
        Object instance = objectMap.get(clazz);
        synchronized (clazz){
            if (instance == null) {
                try {
                    instance = clazz.newInstance();
                    objectMap.put(clazz,instance);
                }catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        return clazz.cast(instance);
    }
}
