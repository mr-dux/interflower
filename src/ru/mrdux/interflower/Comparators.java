package ru.mrdux.interflower;

import java.util.Comparator;

public class Comparators {

    public static Comparator<Object> firstRecordedComparator = (Comparator<Object>) (Object a, Object b) -> {
        if (a == null && b == null) return -1;
        if (a == null) return 1;
        return -1;
    };

    public static Comparator<Object> lastRecordedComparator = (Comparator<Object>) (Object a, Object b) -> {
        if (a == null && b == null) return -1;
        if (b != null) return 1;
        return -1;
    };
}
