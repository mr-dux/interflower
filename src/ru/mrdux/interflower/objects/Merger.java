package ru.mrdux.interflower.objects;

import java.lang.reflect.Field;
import java.util.*;

import ru.mrdux.interflower.objects.MergeHandler.Action;

/* TODO
* 1. Handlers                   √
* 2. CollectionMerger           √
* 3. PrimitiveHandler
* 4. Primitive normal merge
* */

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
        return getResult(objects.get(0));
    }

    public <T extends O> T getResult(T container) {

        if (objects.size() <= 1) return container;

        Class clazz = objects.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        Merge classAnnotation = (Merge) clazz.getAnnotation(Merge.class);

        Map<Field, MergeHandler> mergeFields = fieldsAnalyze(fields, classAnnotation);
        if (mergeFields.isEmpty()) return container;

        for (int i = 0; i < objects.size(); i++) {
            O mergeObj = objects.get(i);
            for (Map.Entry<Field, MergeHandler> e : mergeFields.entrySet()) {
                try {
                    Field field = e.getKey();
                    field.setAccessible(true);
                    MergeHandler handler = e.getValue();
                    Object mergeValue = field.get(mergeObj);
                    Action action = handler.compare(field.get(container), mergeValue);
                    if (action == Action.OVERWRITE) {
                        field.set(container, mergeValue);
                    }
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return container;
    }

    private Map<Field, MergeHandler> fieldsAnalyze(Field[] fields, Merge classAnnotate) {
        Map<Field, MergeHandler> analyze = new HashMap<>();

        for (Field field : fields) {
            boolean merge = isFieldMerge(field, classAnnotate);
            if (!merge) continue;
            MergeHandler handler = getComparator(field, classAnnotate);
            analyze.put(field, handler);
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

    private MergeHandler getComparator(Field field, Merge classAnnotate) {
        MergeField mergeField = field.getAnnotation(MergeField.class);
        if (mergeField != null) {
            String handler = mergeField.handlerName();
            if (!handler.isEmpty()) {
                MergeHandler customComp = options.getCustomHandlers().get(handler);
                if (customComp != null) return customComp;
            }
        }

        String name = field.getName();
        MergeHandler nameComp = options.getFieldHandlers().get(name);
        if (nameComp != null) return nameComp;

        Class type = field.getType();
        MergeHandler typeComp = options.getClassHandlers().get(type);
        if (typeComp != null) return typeComp;

        if (classAnnotate != null) {
            String handler = classAnnotate.handlerName();
            if (!handler.isEmpty()) {
                MergeHandler customComp = options.getCustomHandlers().get(handler);
                if (customComp != null) return customComp;
            }
        }
        return getOptionsComparator();
    }

    private MergeHandler getOptionsComparator() {
        switch (options.getType()) {
            case LAST_RECORDED:
                return Handlers.lastRecordedHandler;
            case DO_NOTHING:
                return Handlers.doNothingHandler;
            default:
                return Handlers.firstRecordedHandler;
        }
    }

}
