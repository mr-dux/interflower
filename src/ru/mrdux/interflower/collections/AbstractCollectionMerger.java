package ru.mrdux.interflower.collections;

import java.util.Arrays;
import java.util.List;

abstract class AbstractCollectionMerger<C, T> {

    List<C> collections;

    boolean noMatch = true;
    Equals<T> equalsHandler = (T o1, T o2)->{
        if (o1 == o2) return true;
        if (o1 == null || o2 == null) return false;
        return o1.equals(o2);
    };

    public AbstractCollectionMerger(List<C> collections) {
        this.collections = collections;
    }

    public AbstractCollectionMerger(C... collections) {
        this(Arrays.asList(collections));
    }

    public void noMatch(boolean noMatch) {
        this.noMatch = noMatch;
    }

    public void setEqualsHandler(Equals<T> equalsHandler) {
        this.equalsHandler = equalsHandler;
    }

    public List<C> getCollections() {
        return collections;
    }

    public void setCollections(List<C> collections) {
        this.collections = collections;
    }

    public void setCollections(C... collections) {
        this.collections = Arrays.asList(collections);
    }

    public void addCollection(C collection) {
        collections.add(collection);
    }

}
