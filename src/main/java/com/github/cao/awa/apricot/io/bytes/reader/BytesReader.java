package com.github.cao.awa.apricot.io.bytes.reader;

public class BytesReader {
    private final byte[] bytes;
    private int cursor = 0;

    public BytesReader(byte[] bytes) {
        this.bytes = bytes;
    }

    public int read() {
        return this.bytes.length > this.cursor ? this.bytes[this.cursor++] : - 1;
    }

    public byte[] read(int length) {
        byte[] result = new byte[length];
        System.arraycopy(
                this.bytes,
                this.cursor,
                result,
                0,
                result.length
        );
        this.cursor += length;
        return result;
    }
}
