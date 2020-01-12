package com.t5eng.miaosha.redis;

public class MiaoshaUserKey extends BasePrefix{

    public  static final int TOKEN_EXPIRE = 3600*24;

    private MiaoshaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE, "token");
    public static MiaoshaUserKey getByName = new MiaoshaUserKey(TOKEN_EXPIRE, "name");

}
