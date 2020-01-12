package com.t5eng.miaosha.util;

import org.junit.Test;

import java.util.UUID;

public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-",""); //取代原生的uuid，去除"-"
    }


    @Test
    public void testUUID(){
        System.out.println(uuid());
    }
}
