package com.wechuang.mallshop.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUtil {

    public static String generateSign(String appid, String secret, Long timestamp) {
        try {
            String signStr = String.join("@", appid, secret, timestamp + "");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(signStr.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte m : messageDigest) {
                hexString.append(String.format("%02x", m));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
