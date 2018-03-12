package ru.mrdux.interflower.collections;

import java.util.*;

/**
 * Temporary solution...
 * At the moment, this class is a wrapper class
 * @see CollectionMerger
 */

public class ArrayMerger<T> extends AbstractCollectionMerger<List<T>, T> {

    private ArrayFilter<T> filterHandler;

    public ArrayMerger() {
    }

    public ArrayMerger(List<T[]> arrays) {
        List<List<T>> list = new ArrayList<>();
        arrays.forEach(arr -> list.add(Arrays.asList(arr)));
        this.collections = list;
    }

    public ArrayMerger(T[]... arrays) {
        this(Arrays.asList(arrays));
    }

    public T[] getResult() {
        CollectionMerger<List<T>, T> collectionMerger = new CollectionMerger<>(collections);
        collectionMerger.noMatch(noMatch);
        collectionMerger.setFilterHandler(wrapToCollectionFilter());
        collectionMerger.setEqualsHandler(equalsHandler);
        List<T> list = collectionMerger.getResultList();
        return (T[]) list.toArray();
    }

    private CollectionFilter<List<T>, T> wrapToCollectionFilter() {
        if (filterHandler == null) return null;
        return (object, ctx) -> {
            T[] container = (T[]) ctx.getContainer().toArray();
            T[] processed = (T[]) ctx.getProcessedContainer().toArray();
            List<T[]> containers = new ArrayList<>();
            ctx.getMergedContainers().forEach(item -> containers.add((T[]) item.toArray()));
            Context<T[], T[]> context = new Context<>(container, processed, containers);
            return filterHandler.filter(object, context);
        };
    }

    public void setFilterHandler(ArrayFilter<T> filterHandler) {
        this.filterHandler = filterHandler;
    }

    public ArrayFilter<T> getFilterHandler() {
        return filterHandler;
    }
}
