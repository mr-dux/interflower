package ru.mrdux.interflower.collections;

import java.util.*;

public class CollectionMerger<C extends Collection<T>, T> extends AbstractCollectionMerger<C, T> {

    private CollectionFilter<C, T> filterHandler;

    public CollectionMerger() {
    }

    public CollectionMerger(List<C> collections) {
        super(collections);
    }

    public CollectionMerger(C... collections) {
        super(collections);
    }

    public <CC extends Collection<T>> CC getResult(CC container) {
        if (collections == null) return null;
        collections.forEach(collection -> {
            collection.forEach(obj -> {
                if (!filter(obj, container, collection)) return;
                if (noMatch) {
                    for (T item : container)
                        if (equalsHandler.equals(obj, item)) return;
                }
                container.add(obj);
            });
        });
        return container;
    }

    private boolean filter(T obj, Collection<T> container, C processedCollection) {
        if (filterHandler == null) return true;
        Context<Collection<T>, C> ctx = new Context<>(container, processedCollection, collections);
        return filterHandler.filter(obj, ctx);
    }

    public C getResult() {
        if (collections == null || collections.isEmpty()) return null;
        C firstElement = collections.get(0);
        if (collections.size() == 1) return firstElement;
        return getResult(firstElement);
    }

    public Set<T> getResultSet() {
        return getResult(new HashSet<T>());
    }

    public List<T> getResultList() {
        return getResult(new ArrayList<T>());
    }

    public void setFilterHandler(CollectionFilter<C, T> filterHandler) {
        this.filterHandler = filterHandler;
    }

    public CollectionFilter<C, T> getFilterHandler() {
        return filterHandler;
    }
}
