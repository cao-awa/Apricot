package com.github.cao.awa.apricot.server.service.plugin.loader;

import com.github.cao.awa.apricot.util.collection.*;
import com.github.cao.awa.apricot.util.io.*;

import java.io.*;
import java.lang.annotation.*;
import java.util.*;
import java.util.jar.*;

public class AnnotatedClassFinder extends ClassLoader {
    private final Class<? extends Annotation> targetAnnotation;
    private final List<File> files = new ArrayList<>();
    private final Map<String, Class<?>> classes = ApricotCollectionFactor.newHashMap();

    public AnnotatedClassFinder(File file, Class<? extends Annotation> targetAnnotation) {
        super(AnnotatedClassFinder.class.getClassLoader());
        this.targetAnnotation = targetAnnotation;
        if (file.isFile()) {
            this.files.add(file);
        } else {
            this.files.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));
        }
        this.load();
    }

    public void load() {
        try {
            for (File file : files) {
                JarFile jar = new JarFile(file);
                Iterator<JarEntry> iterator = jar.entries()
                                                 .asIterator();
                while (iterator.hasNext()) {
                    JarEntry next = iterator.next();
                    try {
                        load(
                                jar,
                                next
                        );
                    } catch (Throwable e) {
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void load(JarFile jar, JarEntry entry) throws IOException {
        if (! entry.isDirectory()) {
            if (entry.getName()
                     .endsWith(".class")) {
                String className = entry.getName();

                className = className.substring(
                                             0,
                                             className.indexOf(".")
                                     )
                                     .replace(
                                             "/",
                                             "."
                                     );

                if (this.classes.containsKey(className)) {
                    return;
                }

                Class<?> target = null;
                try {
                    target = Class.forName(className);
                } catch (Exception | Error e) {

                }

                Class<?> result;
                if (target == null) {
                    byte[] data = IOUtil.readBytes(new BufferedInputStream(jar.getInputStream(entry)));
                    result = defineClass(
                            className,
                            data,
                            0,
                            data.length,
                            null
                    );
                    try {
                        //
                        Class.forName(className, true, this);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    result = target;
                }
                if (result.isAnnotationPresent(this.targetAnnotation)) {
                    this.classes.put(className, result);
                }
            }
        }
    }

    public Map<String, Class<?>> getClasses() {
        return this.classes;
    }

    public Class<?> findClass(String className) {
        try {
            return findSystemClass(className);
        } catch (Exception e) {

        }

        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {

        }
        return this.getClasses().get(className);
    }
}
