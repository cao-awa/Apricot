package com.github.cao.awa.apricot.resource.loader;

import com.github.cao.awa.apricot.anntations.*;

import java.io.*;

@Stable
public class ResourcesLoader {
    public static InputStream get(String resource) {
        return ResourcesLoader.class.getClassLoader().getResourceAsStream(resource);
    }

    public static File asFile(String resource) {
        return new File(String.valueOf(ResourcesLoader.class.getResource(resource)));
    }
}

