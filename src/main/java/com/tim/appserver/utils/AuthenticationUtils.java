package com.tim.appserver.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class AuthenticationUtils {
    private static final String  hashAlgorithmName = "SHA-1";

    public static String encryptPassword(String password, String saltStr) {
        ByteSource salt = ByteSource.Util.bytes(saltStr);
        Object result = new SimpleHash(hashAlgorithmName, password, salt, 1);
        return result.toString();
    }

    public static void main(String[] args) {
        String password = "patience123";
        String saltStr = "RLmbq9yiYci9EsXdojKs58";

        String result = encryptPassword(password, saltStr);
        System.out.println("result:" + result);
    }
}
