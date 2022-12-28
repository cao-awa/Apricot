package com.github.cao.awa.apricot.utils.digger;

import java.io.*;
import java.nio.charset.*;
import java.security.*;

public class MessageDigger {
    public static String digest(String message, DigestAlgorithm sha) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(sha.instanceName());
        messageDigest.update(message.getBytes(StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();
        for (byte b : messageDigest.digest()) {
            String hexString = Integer.toHexString(b & 0xFF);
            if (2 > hexString.length()) {
                result.append(0);
            }
            result.append(hexString);
        }
        return result.toString();
    }

    public static String digestFile(File file, DigestAlgorithm sha) throws Exception {
        int bufSize = 16384;

        RandomAccessFile accessFile = new RandomAccessFile(
                file,
                "r"
        );

        MessageDigest messageDigest = MessageDigest.getInstance(sha.instanceName());

        byte[] buffer = new byte[bufSize];

        long read = 0;

        long offset = accessFile.length();
        int unitsize;
        while (read < offset) {
            unitsize = (int) (((offset - read) < bufSize) ? (offset - read) : bufSize);
            accessFile.read(
                    buffer,
                    0,
                    unitsize
            );

            messageDigest.update(
                    buffer,
                    0,
                    unitsize
            );

            read += unitsize;
        }

        accessFile.close();

        StringBuilder result = new StringBuilder();

        String hexString;
        for (byte b : messageDigest.digest()) {
            hexString = Integer.toHexString(b & 255);
            if (hexString.length() < 2) {
                result.append(0);
            }
            result.append(hexString);
        }

        return result.toString();
    }

    public enum Sha1 implements Sha {
        SHA("SHA-1");

        private final String instance;

        Sha1(String instance) {
            this.instance = instance;
        }

        @Override
        public String instanceName() {
            return instance;
        }
    }

    public enum Sha3 implements Sha {
        SHA_224("SHA3-224"), SHA_256("SHA3-256"), SHA_512("SHA3-512");

        private final String instance;

        Sha3(String instance) {
            this.instance = instance;
        }

        @Override
        public String instanceName() {
            return instance;
        }
    }

    public enum MD4 implements MD {
        MD_4("MD4");

        private final String instance;

        MD4(String instance) {
            this.instance = instance;
        }

        @Override
        public String instanceName() {
            return instance;
        }
    }

    public enum MD5 implements MD {
        MD_5("MD5");

        private final String instance;

        MD5(String instance) {
            this.instance = instance;
        }

        @Override
        public String instanceName() {
            return instance;
        }
    }

    public interface DigestAlgorithm {
        String instanceName();
    }

    public interface Sha extends DigestAlgorithm {
    }

    public interface MD extends DigestAlgorithm {
    }
}

