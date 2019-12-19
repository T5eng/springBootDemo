package com.t5eng.miaosha.redis;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool; //通过bean注入生成 连接池对象

    /**
     * 获取单个对象
     */
    public <T> T get(String key, Class<T>clazz){
        Jedis jedis = null; //通过jedisPool对象获取jedis对象
        try{
            jedis = jedisPool.getResource();
            String str = jedis.get(key);
            T t = stringToBean(str, clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置单个对象
     */
    public <T> boolean set(String key, T value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if(null==str || str.length()<=0){
                return false;
            }
            jedis.set(key, str);
            return true;

        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * bean转String
     */
    public static <T> String beanToString(T value){
        if(null==value) return null;

        Class<?> clazz = value.getClass();//判断不同类型转换为String
        if(clazz==int.class || clazz==Integer.class){
            return ""+value;
        }else if(clazz==String.class){
            return (String)value;
        }else if(clazz==long.class || clazz==Long.class){
            return ""+value;
        }else{
            return JSON.toJSONString(value);
        }
    }

    /**
     * String转bean
     */
    @SuppressWarnings("unchecked")//忽略强制类型转换警告
    public static <T> T stringToBean(String str, Class<T> clazz){
        if (null==str || str.length()<=0 || null==clazz){
            return null;
        }
        if(clazz==int.class || clazz==Integer.class){
            return (T)Integer.valueOf(str);
        }else if(clazz==String.class){
            return (T)str;
        }else if(clazz==long.class || clazz==Long.class){
            return (T)Long.valueOf(str);
        }else{
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }



    public void returnToPool(Jedis jedis){
        if(null!=jedis){
            jedis.close();
        }
    }

}
