package com.wy.rpc.serializer;

public interface CommonSerializer {

    Integer KRYO_SERIALIZER = 0;

    Integer DEFAULT_SERIALIZER = KRYO_SERIALIZER;

    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            default:
                return null;
        }
    }

    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

}