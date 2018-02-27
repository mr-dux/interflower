package ru.mrdux.interflower;

import java.lang.reflect.Field;
import java.util.*;

public class Merger<O> {

    private List<O> objects;
    private Options options;

    public Merger(Options options, List<O> objects) {
        this.options = options;
        this.objects = objects;
    }

    public Merger(Options options, O[] objects) {
        this(options, Arrays.asList(objects));
    }

    public Merger(List<O> objects) {
        this(new Options(), objects);
    }

    public Merger(O[] objects) {
        this(Arrays.asList(objects));
    }

    public O getResult() {
        O container = objects.get(0);
        return getResult(container);
    }

    public O getResult(O container) {

        if (objects.size() <= 1) return container;

        Class clazz = container.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Merge classAnnotation = (Merge) clazz.getAnnotation(Merge.class);

        Map<Field, Comparator<?>> mergeFields = fieldsAnalyze(fields, classAnnotation);
        if (mergeFields.isEmpty()) return container;

        for (int i = 0; i < objects.size(); i++) {
            O mergeObj = objects.get(i);
            for (Map.Entry<Field, Comparator<?>> e : mergeFields.entrySet()) {
                try {
                    Field field = e.getKey();
                    field.setAccessible(true);
                    Comparator c = e.getValue();
                    Object mergeValue = field.get(mergeObj);
                    int rewriteField = c.compare(field.get(container),mergeValue);
                    if (rewriteField > 0){
                        field.set(container, mergeValue);
                    }
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }

            }
        }
        return container;
    }

    private Map<Field, Comparator<?>> fieldsAnalyze(Field[] fields, Merge classAnnotate) {
        Map<Field, Comparator<?>> analyze = new HashMap<Field, Comparator<?>>();

        for (Field field : fields) {
            boolean merge = isFieldMerge(field, classAnnotate);
            if (!merge) continue;
            Comparator<?> comparator = getComparator(field, classAnnotate);
            analyze.put(field, comparator);
        }
        return analyze;
    }

    private boolean isFieldMerge(Field field, Merge classAnnotate) {
        MergeIgnore mergeIgnore = field.getAnnotation(MergeIgnore.class);
        if (mergeIgnore != null) return false;

        if (classAnnotate == null) return true;
        List<String> mergeFields = Arrays.asList(classAnnotate.mergeFields());
        List<String> ignoreField = Arrays.asList(classAnnotate.ignoreField());
        String fieldName = field.getName();

        boolean isInMergeList = mergeFields.contains(fieldName);
        boolean isIgnore = ignoreField.contains(fieldName);
        return !isIgnore && (mergeFields.isEmpty() || isInMergeList);
    }

    private Comparator<?> getComparator(Field field, Merge classAnnotate) {
        MergeField mergeField = field.getAnnotation(MergeField.class);
        if (mergeField != null) {
            String handler = mergeField.handlerName();
            if (!handler.isEmpty()) {
                Comparator<?> customComp = options.getCustomHandlers().get(handler);
                if (customComp != null) return customComp;
            }
        }

        String name = field.getName();
        Comparator<?> nameComp = options.getFieldHandlers().get(name);
        if (nameComp != null) return nameComp;

        Class type = field.getType();
        Comparator<?> typeComp = options.getClassHandlers().get(type);
        if (typeComp != null) return typeComp;

        if (classAnnotate != null) {
            String handler = classAnnotate.handlerName();
            if (!handler.isEmpty()) {
                Comparator<?> customComp = options.getCustomHandlers().get(handler);
                if (customComp != null) return customComp;
            }
        }
        return getOptionsComparator();
    }

    private Comparator<Object> getOptionsComparator() {
        switch (options.getType()) {
            case LAST_RECORDED:
                return Comparators.lastRecordedComparator;
            case DO_NOTHING:
                return Comparators.doNothingComparator;
        }
        return Comparators.firstRecordedComparator;
    }

}
