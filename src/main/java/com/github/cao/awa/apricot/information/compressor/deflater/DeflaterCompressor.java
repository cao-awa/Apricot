package com.github.cao.awa.apricot.information.compressor.deflater;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.information.compressor.*;
import com.github.cao.awa.apricot.util.io.*;

import java.io.*;
import java.util.zip.*;

@Stable
public class DeflaterCompressor implements InformationCompressor {
    public static final DeflaterCompressor INSTANCE = new DeflaterCompressor();

    /**
     * Compress using deflater with best compression
     *
     * @param bytes
     *         Data source
     * @return Compress result
     */
    public byte[] compress(byte[] bytes) {
        if (bytes.length == 0) {
            return EMPTY_BYTES;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            IOUtil.write(
                    new DeflaterOutputStream(
                            out,
                            new Deflater(Deflater.BEST_COMPRESSION)
                    ),
                    bytes
            );
            return out.toByteArray();
        } catch (Exception e) {
            return bytes;
        }
    }

    /**
     * Decompress using inflater
     *
     * @param bytes
     *         Data source
     * @return Decompress result
     */
    public byte[] decompress(byte[] bytes) {
        if (bytes.length == 0) {
            return EMPTY_BYTES;
        }
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            InflaterInputStream inflater = new InflaterInputStream(new ByteArrayInputStream(bytes));
            IOUtil.write(
                    result,
                    inflater
            );
            return result.toByteArray();
        } catch (Exception ex) {
            return bytes;
        }
    }
}
