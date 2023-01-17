package com.github.cao.awa.apricot.resource.dispenser;

import com.github.cao.awa.apricot.database.simple.*;
import com.github.cao.awa.apricot.database.simple.serial.*;
import com.github.cao.awa.apricot.mathematic.base.*;
import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.digger.*;
import com.github.cao.awa.apricot.util.file.*;
import com.github.cao.awa.apricot.util.io.*;
import org.iq80.leveldb.*;
import org.iq80.leveldb.impl.*;

import java.io.*;
import java.net.*;
import java.util.function.*;

public class ResourcesDispenser {
    private final String path;
    private final ApricotServer server;
    private final SerialKvDatabase metadata;
    private final StringLongDatabase files;
    private final ResourceDatabase heads;

    public ResourcesDispenser(String path, ApricotServer server) {
        this.path = path;
        this.server = server;
        try {
            this.metadata = new SerialKvDatabase(
                    path + "/indexes/metadata/metadata.db",
                    8
            );
            this.files = new StringLongDatabase(new Iq80DBFactory().open(
                    new File(path + "/indexes/file"),
                    new Options().createIfMissing(true)
                                 .writeBufferSize(1048560)
                                 .compressionType(CompressionType.SNAPPY)
            ));
            this.heads = new ResourceDatabase(
                    new Iq80DBFactory().open(
                            new File(path + "/indexes/head"),
                            new Options().createIfMissing(true)
                                         .writeBufferSize(1048560)
                                         .compressionType(CompressionType.SNAPPY)
                    ),
                    new Iq80DBFactory().open(
                            new File(path + "/indexes/convert"),
                            new Options().createIfMissing(true)
                                         .writeBufferSize(1048560)
                                         .compressionType(CompressionType.SNAPPY)
                    )
            );

            byte[] mainKey = this.metadata.get(0L);
            if (mainKey == null || mainKey.length == 0) {
                this.metadata.set(
                        0L,
                        Base256.longToBuf(0L)
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getPath() {
        return this.path;
    }

    public ApricotServer getServer() {
        return this.server;
    }

    public void download(String name, String url) {
        download(
                name,
                url,
                success -> {
                }
        );
    }

    public void download(String name, String url, Consumer<Boolean> callback) {
        if (get(name).isFile()) {
            callback.accept(true);
            return;
        }
        File file = new File(this.path + name);
        FileUtil.mkdirs(file.getParentFile());
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)"
            );

            InputStream input = connection.getInputStream();

            FileOutputStream output = new FileOutputStream(file);

            IOUtil.write(
                    output,
                    input,
                    new byte[4096]
            );

            String sha = MessageDigger.digestFile(
                    file,
                    MessageDigger.Sha3.SHA_512
            );

            File shaFile = new File(this.path + sha);

            String targetSha = MessageDigger.digestFile(
                    shaFile,
                    MessageDigger.Sha3.SHA_512
            );

            if (! sha.equals(targetSha)) {
                IOUtil.copy(
                        file,
                        shaFile
                );
            }

            long shaPos = this.heads.get(sha);

            if (shaPos == - 1) {
                shaPos = Base256.longFromBuf(this.metadata.get(0L));
                this.metadata.set(
                        0L,
                        Base256.longToBuf(shaPos + 1)
                );
                this.heads.set(
                        sha,
                        shaPos
                );
            }

            this.files.set(
                    name,
                    shaPos
            );

            callback.accept(true);

            file.delete();
        } catch (Exception ignored) {
            file.delete();
            callback.accept(false);
        }
    }

    public File get(String name) {
        try {
            String sha = this.heads.getConvert(this.files.get(name));
            if (! sha.equals("")) {
                return new File(this.path + sha);
            }
        } catch (Exception ignored) {
        }
        return new File(this.path + name);
    }
}
