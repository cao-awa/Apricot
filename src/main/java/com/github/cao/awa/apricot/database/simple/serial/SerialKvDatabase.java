package com.github.cao.awa.apricot.database.simple.serial;

import com.github.cao.awa.apricot.anntations.*;
import com.github.cao.awa.apricot.database.*;
import com.github.cao.awa.apricot.util.file.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.io.*;
import java.util.function.*;

@Synchronized
public class SerialKvDatabase extends ApricotDatabase<Long, byte[]> {
    private final int size;
    private final byte[] buf;
    private final RandomAccessFile file;
    private long id;

    public SerialKvDatabase(String file, int size) {
        this.size = size;
        this.buf = new byte[size];
        FileUtil.mkdirsParent(new File(file));

        this.file = EntrustEnvironment.get(
                () -> new RandomAccessFile(
                        file,
                        "rw"
                ),
                null
        );
        this.id = size();
    }

    public long size() {
        try {
            return this.file.length() / this.size;
        } catch (Exception e) {
            return - 1;
        }
    }

    @Override
    public void forEach(BiConsumer<Long, byte[]> action) {
        synchronized (this) {
            try {
                this.file.seek(0);
                long length = this.file.length();
                long key = 0;
                while (length > this.file.getFilePointer()) {
                    this.file.read(this.buf);
                    action.accept(
                            key,
                            this.buf
                    );
                    key++;
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void set(Long key, byte[] value) {
        synchronized (this) {
            try {
                if (key < 0) {
                    return;
                }
                this.file.seek(key * this.size);
                this.file.write(value);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public byte[] get(Long key) {
        synchronized (this) {
            try {
                this.file.seek(key * this.size);
                this.file.read(this.buf);
                return this.buf;
            } catch (Exception e) {
                return new byte[this.size];
            }
        }
    }

    public byte[] delete(Long key) {
        synchronized (this) {
            try {
                // Get last value
                byte[] result = get(key);

                // Seek to position
                long writePos = getPos(key);
                long readPos = getPos(key + 1);
                int bufSize = 4096;
                this.file.seek(readPos);
                byte[] bytes = new byte[bufSize];
                int length;
                // Write data
                while ((length = this.file.read(bytes)) != - 1) {
                    this.file.seek(writePos);
                    this.file.write(
                            bytes,
                            0,
                            length
                    );
                    writePos += bufSize;
                    readPos += bufSize;
                    this.file.seek(readPos);
                }

                this.file.setLength((-- this.id) * this.size);

                return result;
            } catch (Exception e) {
                return new byte[this.size];
            }
        }
    }

    private long getPos(long id) {
        return id * this.size;
    }

    public void append(byte[] value) {
        set(
                this.id++,
                value
        );
    }

    public void deletes(Long from, Long to) {
        synchronized (this) {
            try {
                // Seek to position
                long writePos = getPos(from);
                long readPos = getPos(to + 1);
                int bufSize = 4096;
                this.file.seek(readPos);
                byte[] bytes = new byte[bufSize];
                int length;
                // Write data
                while ((length = this.file.read(bytes)) != - 1) {
                    this.file.seek(writePos);
                    this.file.write(
                            bytes,
                            0,
                            length
                    );
                    writePos += bufSize;
                    readPos += bufSize;
                    this.file.seek(readPos);
                }

                this.file.setLength((this.id -= (to - from)) * this.size);
            } catch (Exception e) {
            }
        }
    }
}
