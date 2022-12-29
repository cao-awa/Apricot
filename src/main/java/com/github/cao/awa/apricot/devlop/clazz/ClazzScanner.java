package com.github.cao.awa.apricot.devlop.clazz;

import com.github.cao.awa.apricot.utils.collection.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;

import java.io.*;
import java.lang.annotation.*;
import java.net.*;
import java.util.*;

public class ClazzScanner {
    private final List<Class<?>> classes;

    public ClazzScanner(Class<?> clazz) {
        this.classes = getAllAssignedClass(clazz);
    }

    public List<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> clazz) {
        List<Class<?>> list = ApricotCollectionFactor.newArrayList();
        EntrustEnvironment.tryFor(this.classes, c -> {
            if (c.isAnnotationPresent(clazz)) {
                list.add(c);
            }
        });
        return list;
    }

    public List<Class<?>> getAllAssignedClass(Class<?> clazz) {
        List<Class<?>> classes = ApricotCollectionFactor.newArrayList();
        EntrustEnvironment.tryFor(getClasses(clazz), c -> {
            if (clazz.isAssignableFrom(c) && ! clazz.equals(c)) {
                classes.add(c);
            }
        });
        return classes;
    }

    public List<Class<?>> getClasses(Class<?> cls) {
        String pk = cls.getPackage().getName();
        String path = pk.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource(path);
        return getClasses(new File(url.getFile()), pk);
    }

    private List<Class<?>> getClasses(File dir, String path) {
        List<Class<?>> classes = ApricotCollectionFactor.newArrayList();
        if (! dir.exists()) {
            return classes;
        }
        EntrustEnvironment.tryFor(dir.listFiles(), file -> {
            if (file.isDirectory()) {
                classes.addAll(getClasses(file, path + "." + file.getName()));
            }
            String name = file.getName();
            if (name.endsWith(".class")) {
                classes.add(Class.forName(path + "." + name.substring(0, name.length() - 6)));
            }
        });
        return classes;
    }

    public List<Class<?>> getClasses() {
        return this.classes;
    }
}

