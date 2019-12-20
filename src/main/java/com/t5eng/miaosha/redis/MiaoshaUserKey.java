package com.t5eng.miaosha.redis;

public class MiaoshaUserKey extends BasePrefix{
    private MiaoshaUserKey(String prefix) {
        super(prefix);
    }

    public static MiaoshaUserKey tocken = new MiaoshaUserKey("tk");
    public static MiaoshaUserKey getByName = new MiaoshaUserKey("name");

}
