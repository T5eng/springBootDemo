package com.t5eng.miaosha.redis;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.context.annotation.Bean;
        import org.springframework.stereotype.Service;
        import redis.clients.jedis.JedisPool;
        import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisPoolFactory {
    @Autowired
    RedisConfig redisConfig; //自己创建的类

    @Bean
    public JedisPool JedisPoolFactory() {//根据redisConfig中的参数生成jedis类
        JedisPoolConfig poolConfig = new JedisPoolConfig(); //Jedis的自带的类
        poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);
        JedisPool jp = new JedisPool(poolConfig,
                redisConfig.getHost(),
                redisConfig.getPort(),
                redisConfig.getTimeout() * 1000, //socket的超时，以ms为单位
                redisConfig.getPassword(), 0);
        return jp;
    }
}
