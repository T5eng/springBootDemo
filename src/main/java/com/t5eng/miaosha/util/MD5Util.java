package com.t5eng.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util { //将明文密码md5加密, 传入DataBase前再次md5加密

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    public static String inputToPass(String inputPass){ //第一次加密（密码+固定盐值），前端，避免明文传输
        String tmp = salt.charAt(0)+salt.charAt(2)+ inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(tmp);
    }

    public static String PassToDBPass(String inputPass, String salt){ //第二次加密（密码+随机盐值），服务端，避免前端被爆破
        String tmp = salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(tmp);
    }

    public static String inputPassToDbPass(String input, String saltDB){ //封装2次加密
        String md5Pass = inputToPass(input);
        String DBPass = PassToDBPass(md5Pass, saltDB);
        return DBPass;
    }


    public static void main(String[] args) {
        System.out.println(inputToPass("00000000"));
        System.out.println(PassToDBPass("00000000", salt));
        System.out.println(inputPassToDbPass("00000000", salt));
    }
}
