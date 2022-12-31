package com.github.cao.awa.apricot.utils.collection;

import it.unimi.dsi.fastutil.objects.*;

import java.util.*;
import java.util.concurrent.*;

public class ApricotCollectionFactor {
    public static <K, V> Map<K, V> newHashMap() {
//         return new HashMap<>();
        return new Object2ObjectOpenHashMap<>();
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

    public static <V> List<V> newArrayList() {
        return newArrayList(0);
    }

    public static <V> List<V> newArrayList(int capacity) {
//         return new ArrayList<>(capacity);
        return new ObjectArrayList<>(capacity);
    }

    public static <V> List<V> newLinkedList() {
         return new LinkedList<>();
    }

    public static <V> Set<V> newHashSet() {
//         return new HashSet<>();
         return new ObjectOpenHashSet<>();
    }
}
