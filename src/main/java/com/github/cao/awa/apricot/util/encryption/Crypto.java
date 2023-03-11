package com.github.cao.awa.apricot.util.encryption;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Stable
public class Crypto {
    private static final Logger DEBUG = LogManager.getLogger("Debugger");
    private static final byte[] KEY_VI = "0000000000000000".getBytes();

    static {
        Security.setProperty("crypto.policy",
                             "unlimited"
        );
    }

    public static byte[] decrypt(byte[] content, byte[] cipher) throws Exception {
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(Cipher.DECRYPT_MODE,
                      new SecretKeySpec(cipher,
                                        "AES"
                      ),
                      new IvParameterSpec(KEY_VI)
        );
        return instance.doFinal(content);
    }

    public static byte[] encrypt(byte[] content, byte[] cipher) throws Exception {
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(Cipher.ENCRYPT_MODE,
                      new SecretKeySpec(cipher,
                                        "AES"
                      ),
                      new IvParameterSpec(KEY_VI)
        );
        return instance.doFinal(content);
    }

    public static void main(String[] args) {
        try {
            String message = "xxx";

            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(8192);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

            long start = TimeUtil.millions();

            byte[] e = encrypt(message.getBytes(),
                               publicKey
            );

            DEBUG.info("Encrypt done in {}ms",
                       TimeUtil.processMillion(start)
            );
            DEBUG.info("Message：" + new String(e));

            start = TimeUtil.millions();

            byte[] de = decrypt(e,
                                privateKey
            );

            DEBUG.info("Decrypt done in {}ms",
                       TimeUtil.processMillion(start)
            );
            DEBUG.info("Message：" + new String(de));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(byte[] content, RSAPublicKey publicKey) throws Exception {
        Cipher instance = Cipher.getInstance("RSA");
        instance.init(Cipher.ENCRYPT_MODE,
                      publicKey
        );
        return instance.doFinal(content);
    }

    public static byte[] decrypt(byte[] content, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,
                    privateKey
        );
        return cipher.doFinal(content);
    }
}
