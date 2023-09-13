package com.github.cao.awa.apricot.util.collection;

import java.util.*;
import java.util.concurrent.*;

public class ApricotCollectionFactor {
    public static <K, V> Map<K, V> hashMap() {
        return new HashMap<>();
        //        return new Object2ObjectOpenHashMap<>();
    }

    public static <K, V> Map<K, V> hashMap(Map<K, V> map) {
        return new HashMap<>(map);
        //        return new Object2ObjectOpenHashMap<>(map);
    }

    public static <K, V> ConcurrentHashMap<K, V> concurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

    public static <V> List<V> arrayList() {
        return new ArrayList<>();
        //        return new ObjectArrayList<>();
    }

    public static <V> List<V> arrayList(int capacity) {
        return new ArrayList<>(capacity);
        //        return new ObjectArrayList<>(capacity);
    }

    public static <V> List<V> linkedList() {
        return new LinkedList<>();
    }

    public static <V> Set<V> hashSet() {
        return new HashSet<>();
        //         return new ObjectOpenHashSet<>();
    }
}
