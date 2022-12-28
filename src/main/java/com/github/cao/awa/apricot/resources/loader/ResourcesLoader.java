package com.github.cao.awa.apricot.resources.loader;

import java.io.*;

public class ResourcesLoader {
    public static InputStream getResource(String resource) {
        return ResourcesLoader.class.getClassLoader().getResourceAsStream(resource);
    }

    public static File getResourceByFile(String resource) {
        return new File(String.valueOf(ResourcesLoader.class.getResource(resource)));
    }
}

