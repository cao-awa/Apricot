package com.github.cao.awa.apricot.server.service.plugin.loader;

import bot.inker.acj.*;
import com.github.cao.awa.apricot.util.collection.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;

public class JarSearchLoader {
    public static List<URL> load(@NotNull File file) {
        final List<URL> urls = ApricotCollectionFactor.arrayList();
        List<File> jars = ApricotCollectionFactor.arrayList();
        if (file.exists()) {
            if (file.isFile()) {
                jars.add(file);
            } else {
                jars.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));
            }

            try {
                for (File jarFile : jars) {
                    JarFile jar = new JarFile(jarFile);
                    JvmHacker.instrumentation()
                             .appendToSystemClassLoaderSearch(jar);
                    urls.add(jarFile.toURI()
                                    .toURL());
                }
            } catch (Exception ignored) {
            }
        }
        return urls;
    }
}
