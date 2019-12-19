package com.t5eng.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }
    private static final String salt = "1a2b3c4d";

    public static String inputToPass(String inputPass){
        String tmp = salt.charAt(0)+salt.charAt(2)+ inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(tmp);
    }

    public static String PassToDBPass(String inputPass, String salt){
        String tmp = salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(tmp);
    }

    public static String inputPassToDbPass(String input, String saltDB){
        String md5Pass = inputToPass(input);
        String DBPass = PassToDBPass(md5Pass, saltDB);
        return DBPass;
    }


    public static void main(String[] args) {
//        System.out.println(inputPassFromPass("abc"));
//        System.out.println(fromPassToDBPass("abc", salt));
        System.out.println(inputPassToDbPass("abc", salt));
    }
}
