package com.github.cao.awa.apricot.database.simple;

import com.github.cao.awa.apricot.database.*;
import com.github.cao.awa.apricot.math.base.*;
import com.github.cao.awa.apricot.utils.file.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.function.*;

public class SerialLongKvDatabase extends ApricotDatabase<Long, Long> {
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
            e.printStackTrace();
        }
    }

    @Override
    public void forEach(BiConsumer<Long, Long> action) {
        try {
            this.file.seek(0);
            long length = this.file.length();
            long key = 0;
            while (length > this.file.getFilePointer()) {
                byte[] value = new byte[8];
                this.file.read(value);
                action.accept(
                        key,
                        Base256.longFromBuf(value)
                );
                key++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(@Range(from = - 1L, to = - 1L) Long key, Long value) {
        try {
            if (key != - 1) {
                return;
            }
            byte[] bytes = Base256.longToBuf(value);
            this.file.seek(this.id * 8);
            this.file.write(bytes);

            this.id++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long get(Long key) {
        try {
            byte[] value = new byte[8];
            this.file.seek(key * 8);
            this.file.read(value);
            return Base256.longFromBuf(value);
        } catch (Exception e) {
            return - 1L;
        }
    }

    public void append(Long value) {
        put(
                - 1L,
                value
        );
    }
}
