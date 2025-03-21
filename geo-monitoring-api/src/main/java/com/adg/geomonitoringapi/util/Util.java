package com.adg.geomonitoringapi.util;

import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Util {
    public static final Instant SOME_TIMESTAMP_IN_FUTURE = Instant.ofEpochSecond(31556889864L);

    public static <K, V> TreeMap<K, V> limit(TreeMap<K, V> map, Long limit) {
        return map.descendingMap().entrySet().stream()
                .limit(limit)
                .collect(TreeMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        TreeMap::putAll);
    }

    public static synchronized <T> List<T> atomicallyExtractAll(Queue<T> queue) {
        List<T> elements = new ArrayList<>();
        for (T e = queue.poll(); e != null; e = queue.poll())
            elements.add(e);
        return elements;
    }

    public static <T, R> Set<R> construct(Map<Supplier<Boolean>, R> candidates) {
        return candidates.entrySet().stream()
                .filter(e -> e.getKey().get())
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }
}
