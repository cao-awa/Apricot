package com.github.cao.awa.apricot.resource.dispenser;

import com.github.cao.awa.apricot.server.*;
import com.github.cao.awa.apricot.util.file.*;

import java.io.*;
import java.net.*;
import java.util.function.*;

public class ResourcesDispenser {
    private final String path;
    private final ApricotServer server;

    public ResourcesDispenser(String path, ApricotServer server) {
        this.path = path;
        this.server = server;
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
        FileUtil.mkdirs(name);
        File file = get(name);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)"
            );

            InputStream input = connection.getInputStream();

            FileOutputStream output = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int length;
            while ((length = input.read(buffer)) != - 1) {
                output.write(
                        buffer,
                        0,
                        length
                );
            }
            output.close();
            input.close();

            callback.accept(true);
        } catch (Exception ignored) {
            file.delete();
            callback.accept(false);
        }
    }

    public File get(String name) {
        return new File(this.path + name);
    }
}
