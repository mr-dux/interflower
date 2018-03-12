package ru.mrdux.interflower.collections;

import java.util.List;

public interface ArrayFilter<T>{
    boolean filter(T object, Context<T[], T[]> ctx);
}
