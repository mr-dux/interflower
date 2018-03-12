package ru.mrdux.interflower.collections;

import java.util.Map;

public interface MapFilter<K, V> {
    boolean filter(Map.Entry<K, V> entry, Context<Map<K, V>, Map<K, V>> ctx);
}
