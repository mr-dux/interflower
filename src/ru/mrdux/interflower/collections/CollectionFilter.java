package ru.mrdux.interflower.collections;

import java.util.Collection;

public interface CollectionFilter<C extends Collection<T>, T>{
    boolean filter(T object, Context<Collection<T>, C> ctx);
}
