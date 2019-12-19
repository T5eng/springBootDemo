package com.t5eng.miaosha.redis;

public abstract class BasePrefix implements KeyPrefix{

    private int expireSecond;
    private String prefix;

    public BasePrefix(String prefix){//默认0代表永不过期
        this(0, prefix);
    }

    public BasePrefix(int expireSecond, String prefix){
        this.expireSecond = expireSecond;
        this.prefix = prefix;
    }


    @Override
    public int expireSecond() {//默认0代表永不过期
        return this.expireSecond;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + this.prefix;
    }
}
