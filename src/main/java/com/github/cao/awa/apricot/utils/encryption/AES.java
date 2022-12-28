package com.github.cao.awa.apricot.utils.encryption;

import org.apache.commons.codec.binary.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;

public class AES {
    private static final byte[] KEY_VI = "0000000000000000".getBytes();

    static {
        Security.setProperty("crypto.policy", "unlimited");
    }

    public static String encryptToString(byte[] content, byte[] key) throws Exception {
        return StringUtils.newStringIso8859_1(encrypt(content, key));
    }

    public static String decryptToString(byte[] content, byte[] key) throws Exception {
        return StringUtils.newStringIso8859_1(decrypt(content, key));
    }

    public static byte[] decrypt(byte[] content, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(KEY_VI));
        return cipher.doFinal(Base64.decodeBase64(content));
    }

    public static byte[] encrypt(byte[] content, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(KEY_VI));
        return Base64.encodeBase64(cipher.doFinal(content));
    }
}
