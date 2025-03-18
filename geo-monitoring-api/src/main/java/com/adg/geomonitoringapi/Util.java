package com.adg.geomonitoringapi;

import com.adg.geomonitoringapi.event.entity.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.TreeMap;

public class Util {
    public static <K, V> TreeMap<K, V> limit(TreeMap<K, V> map, Long limit) {
        return map.descendingMap().entrySet().stream()
                .limit(limit)
                .collect(TreeMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        TreeMap::putAll);
    }

    public static <T> List<T> extractAll(Queue<T> queue) {
        List<T> elements = new ArrayList<>();
        for (T e = queue.poll(); e != null; e = queue.poll())
            elements.add(e);
        return elements;
    }
}
