package com.t5eng.miaosha.redis;

public interface KeyPrefix { // 接口（KeyPrefix） <- 抽象类（BasePrefix） <- 具体类（MiaoshaUserKey，OrderKey，UserKey）
    public int expireSecond(); //根据前缀控制redis缓存的有效时间
    public String getPrefix(); //返回前缀
}
