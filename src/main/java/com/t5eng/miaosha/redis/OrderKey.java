package com.t5eng.miaosha.redis;

public class OrderKey extends BasePrefix{
    public OrderKey(int expireSecond, String prefix) {
        super(expireSecond, prefix);
    }
}
