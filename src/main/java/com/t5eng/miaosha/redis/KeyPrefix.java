package com.t5eng.miaosha.redis;

public interface KeyPrefix {
    public int expireSecond();
    public String getPrefix();
}
