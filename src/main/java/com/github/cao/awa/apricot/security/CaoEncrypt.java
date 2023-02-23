package com.github.cao.awa.apricot.security;

import com.github.cao.awa.apricot.util.io.IOUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * Simple encrypt and decrypt.
 * <p>
 * This encryption can decrypt using exactly the same process.
 * <p>
 * Experimental algorithm, do not heavily used.
 *
 * @author cao_awa
 * @since 1.0.0
 */
public class CaoEncrypt {
    private static final Logger LOGGER = LogManager.getLogger("CaoEncrypt");

    public static void partFile(File file, File toFile, byte[] key, int roundCount, int blockSize) throws IOException {
        if (! file.isFile()) {
            return;
        }
        if (! toFile.isFile()) {
            toFile.createNewFile();
        }
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(toFile));
        int length;
        int blocks = 0;
        byte[] block = new byte[blockSize];
        while ((length = input.read(block)) != - 1) {
            if (length != blockSize) {
                byte[] newBlock = new byte[length];
                System.arraycopy(block,
                                 0,
                                 newBlock,
                                 0,
                                 length
                );
                block = newBlock;
            }
            part(block,
                 key,
                 roundCount,
                 blocks
            );

            IOUtil.write0(output,
                          block
            );

            blocks++;
        }

        output.close();
        input.close();
    }

    public static void part(byte[] bytes, byte[] key, int roundCount, int inherit) {
        // The round must be odd number.
        if ((roundCount & 1) == 0) {
            roundCount++;
        }
        int keyIndex = rTdr(inherit,
                            key.length
        );
        long offs = inherit;
        int flipper = ~ roundCount;
        // Round encrypt and decrypt, the stream feature replace need a few rounds.
        for (int r = 0; r < roundCount; r++) {
            // Chaos step.
            for (int i = 0; i < bytes.length; i++) {
                // Up the stream, for next feature replaces.
                offs++;
                // The key to effects result, use loop key.
                if (keyIndex == key.length) {
                    keyIndex = 0;
                }
                // Using stream feature replaced to prevent the feature extraction.
                // The 'offs' is stream primary.
                bytes[i] ^= r;
                bytes[i] ^= offs;
                bytes[i] ^= key[keyIndex] ^ (offs += key[keyIndex]);
                bytes[i] ^= key[rTdr(offs,
                                     key.length
                )];
                bytes[i] = (byte) (~ bytes[i]);
                keyIndex++;
            }

            // The flipper is sub stream.
            // Here is making more cost to encrypt and decrypt.
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) ~ (~ bytes[i] ^ (flipper = ~ flipper));
                bytes[i] = (byte) ~ bytes[i];
            }

            // Chaos step.
            for (int i = bytes.length - 1; i > 0; i--) {
                // Up the stream, for next feature replaces.
                offs++;
                // The key to effects result, use loop key.
                if (keyIndex == key.length) {
                    keyIndex = 0;
                }
                // Make  more  chaos, further hinder the  feature extraction.
                bytes[i] ^= ~ r;
                bytes[i] ^= (~ offs) ^ i;
                bytes[i] ^= key[keyIndex] ^ (offs += key[keyIndex]);
                bytes[i] ^= key[rTdr(offs,
                                     key.length
                )];
                bytes[i] = (byte) ((~ bytes[i]) ^ offs);
                keyIndex++;
            }

            // Swaps the bytes.
            // Here is making more cost to encrypt and decrypt.
            for (int i = 1; i < bytes.length; i++) {
                int rIndex = bytes.length - i;
                byte lOrin = bytes[i];
                byte rOrin = bytes[rIndex];
                bytes[i] = rOrin;
                bytes[rIndex] = lOrin;
            }

            // Move key.
            byte first = key[0];
            System.arraycopy(key,
                             1,
                             key,
                             0,
                             key.length - 1
            );
            key[key.length - 1] = first;
        }
    }

    public static int rTdr(long source, int target) {
        int result = (int) (source % target);
        while (result < 0) {
            result = (- result % target);
        }
        return result;
    }
}
