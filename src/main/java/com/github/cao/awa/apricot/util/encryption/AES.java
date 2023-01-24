package com.github.cao.awa.apricot.util.encryption;

import com.github.cao.awa.apricot.anntations.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;

@Stable
public class AES {
    private static final byte[] KEY_VI = "0000000000000000".getBytes();

    static {
        Security.setProperty("crypto.policy", "unlimited");
    }

    public static byte[] decrypt(byte[] content, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(KEY_VI));
        return cipher.doFinal(content);
    }

    public static byte[] encrypt(byte[] content, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(KEY_VI));
        return cipher.doFinal(content);
    }
}
