package com.t5eng.miaosha.redis;


import com.alibaba.fastjson.JSON;
import com.t5eng.miaosha.domain.MiaoshaUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool; //通过bean注入生成 连接池对象

    /**
     * 获取单个对象
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T>clazz){
        Jedis jedis = null; //通过jedisPool对象获取jedis对象
        try{
            jedis = jedisPool.getResource();
            //拼接前缀
            key = prefix.getPrefix()+key;
            String str = jedis.get(key);
            T t = stringToBean(str, clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置对象
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if(null==str || str.length()<=0){
                return false;
            }
            // 拼接前缀
            key = prefix.getPrefix()+key;
            // 获取缓存有效时间
            int seconds = prefix.expireSecond();
            if(seconds<=0){
                jedis.set(key, str);
            }else{
                jedis.setex(key,seconds,str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断存在与否
     */
    public <T> boolean exists(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //拼接前缀
            key = prefix.getPrefix()+key;
            return jedis.exists(key);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * key对应的value值++
     */
    public <T> Long incr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //拼接前缀
            key = prefix.getPrefix()+key;
            return jedis.incr(key);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * key对应的value值--
     */
    public <T> Long decr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //拼接前缀
            key = prefix.getPrefix()+key;
            return jedis.decr(key);
        }finally {
            returnToPool(jedis);
        }
    }


    public boolean delete(KeyPrefix prefix) {
        if(prefix == null) {
            return false;
        }
        List<String> keys = scanKeys(prefix.getPrefix());
        if(keys==null || keys.size() <= 0) {
            return true;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(keys.toArray(new String[0]));
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            returnToPool(jedis);
        }
    }

    public List<String> scanKeys(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> keys = new ArrayList<String>();
            String cursor = "0";
            ScanParams sp = new ScanParams();
            sp.match("*"+key+"*");
            sp.count(100);
            do{
                ScanResult<String> ret = jedis.scan(cursor, sp);
                List<String> result = ret.getResult();
                if(result!=null && result.size() > 0){
                    keys.addAll(result);
                }
                //再处理cursor
                cursor = ret.getStringCursor();
            }while(!cursor.equals("0"));
            return keys;
        } finally {
            returnToPool(jedis);
        }
    }


    /**
     * bean转String 序列化
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
            return JSON.toJSONString(value); //JSON序列化
        }
    }

    /**
     * String转bean 反序列化
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
            return JSON.toJavaObject(JSON.parseObject(str), clazz); //JSON反序列化
        }
    }

    public void returnToPool(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }


}
