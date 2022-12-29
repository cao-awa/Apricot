package com.github.cao.awa.apricot.devlop.clazz;

import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import it.unimi.dsi.fastutil.objects.*;

import java.io.*;
import java.lang.annotation.*;
import java.net.*;

public class ClazzScanner {
    private final ObjectArrayList<Class<?>> classes;

    public ClazzScanner(Class<?> clazz) {
        this.classes = getAllAssignedClass(clazz);
    }

    public ObjectArrayList<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> clazz) {
        ObjectArrayList<Class<?>> list = new ObjectArrayList<>();
        EntrustEnvironment.tryFor(this.classes, c -> {
            if (c.isAnnotationPresent(clazz)) {
                list.add(c);
            }
        });
        return list;
    }

    public ObjectArrayList<Class<?>> getAllAssignedClass(Class<?> clazz) {
        ObjectArrayList<Class<?>> classes = new ObjectArrayList<>();
        EntrustEnvironment.tryFor(getClasses(clazz), c -> {
            if (clazz.isAssignableFrom(c) && ! clazz.equals(c)) {
                classes.add(c);
            }
        });
        return classes;
    }

    public ObjectArrayList<Class<?>> getClasses(Class<?> cls) {
        String pk = cls.getPackage().getName();
        String path = pk.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource(path);
        return getClasses(new File(url.getFile()), pk);
    }

    private ObjectArrayList<Class<?>> getClasses(File dir, String path) {
        ObjectArrayList<Class<?>> classes = new ObjectArrayList<>();
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

    public ObjectArrayList<Class<?>> getClasses() {
        return this.classes;
    }
}

