package ru.mrdux.interflower.collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MapMerger<K, V> extends AbstractCollectionMerger<Map<K, V>, V> {

    private MapFilter<K, V> filterHandler;

    public MapMerger() {
    }

    public MapMerger(List<Map<K, V>> collections) {
        super(collections);
    }

    public MapMerger(Map<K, V>... collections) {
        super(collections);
    }

    public <M extends Map<K, V>> M getResult(M container) {
        if (collections == null) return container;
        collections.forEach(map -> {
            if (map == null) return;
            for (Map.Entry<K, V> entry : map.entrySet()){
                if (!filter(entry, container, map)) return;
                if (noMatch) {
                    for (Map.Entry<K, V> e : container.entrySet())
                        if (equalsHandler.equals(entry.getValue(), e.getValue())) return;
                }
                container.put(entry.getKey(),entry.getValue());
            }
        });
        return container;
    }

    private boolean filter(Map.Entry<K, V> entry, Map<K, V> container, Map<K, V> processedCollection) {
        if (filterHandler == null) return true;
        Context<Map<K, V>, Map<K, V>> ctx = new Context<>(container, processedCollection, collections);
        return filterHandler.filter(entry, ctx);
    }

    public Map<K, V> getResult() {
        if (collections == null || collections.isEmpty()) return null;
        Map<K, V> firstElement = collections.get(0);
        if (collections.size() == 1) return firstElement;
        return getResult(firstElement);
    }

    public HashMap<K, V> getResultHashMap() {
        return getResult(new HashMap<>());
    }

    public TreeMap<K, V> getResultTreeMap() {
        return getResult(new TreeMap<>());
    }

    public void setFilterHandler(MapFilter<K, V> filterHandler) {
        this.filterHandler = filterHandler;
    }

    public MapFilter<K, V> getFilterHandler() {
        return filterHandler;
    }
}
