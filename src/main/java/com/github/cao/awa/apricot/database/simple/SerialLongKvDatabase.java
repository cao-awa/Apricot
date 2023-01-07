package com.github.cao.awa.apricot.database.simple;

import com.github.cao.awa.apricot.database.*;
import com.github.cao.awa.apricot.math.base.*;
import com.github.cao.awa.apricot.utils.file.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.io.*;
import java.util.function.*;

public class SerialLongKvDatabase extends ApricotDatabase<Long, Long> {
    private final byte[] buf = new byte[8];
    private final RandomAccessFile file;
    private long id = 0;

    public SerialLongKvDatabase(String dbFile) {
        FileUtil.mkdirsParent(new File(dbFile));

        this.file = EntrustEnvironment.get(
                () -> new RandomAccessFile(
                        dbFile,
                        "rw"
                ),
                null
        );
        try {
            this.id = this.file.length() / 8;
        } catch (Exception e) {
        }
    }

    @Override
    public void forEach(BiConsumer<Long, Long> action) {
        synchronized (this) {
            try {
                this.file.seek(0);
                long length = this.file.length();
                long key = 0;
                while (length > this.file.getFilePointer()) {
                    this.file.read(this.buf);
                    action.accept(
                            key,
                            Base256.longFromBuf(this.buf)
                    );
                    key++;
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void set(Long key, Long value) {
        synchronized (this) {
            try {
                if (key < 0) {
                    return;
                }
                Base256.longToBuf(value, this.buf);
                this.file.seek(key * 8);
                this.file.write(this.buf);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public Long get(Long key) {
        synchronized (this) {
            try {
                byte[] value = new byte[8];
                this.file.seek(key * 8);
                this.file.read(value);
                return Base256.longFromBuf(value);
            } catch (Exception e) {
                return - 1L;
            }
        }
    }

    public Long delete(Long key) {
        synchronized (this) {
            try {
                // Get last value
                long result = get(key);

                // Seek to position
                long writePos = getPos(key);
                long readPos = getPos(key + 1) * 8;
                int bufSize = 4096;
                this.file.seek(readPos);
                byte[] bytes = new byte[bufSize];
                int length;
                // Write data to tmp file
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

                this.file.setLength((-- this.id) * 8);

                return result;
            } catch (Exception e) {
                return - 1L;
            }
        }
    }

    private long getPos(long id) {
        return id * 8;
    }

    public void append(Long value) {
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
                // Write data to tmp file
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

                this.file.setLength((this.id -= (to - from)) * 8);
            } catch (Exception e) {
            }
        }
    }
}
