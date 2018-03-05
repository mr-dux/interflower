package ru.mrdux.interflower;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Options {

    private Map<String, Comparator<?>> customHandlers;
    private Map<String, Comparator<?>> fieldHandlers;
    private Map<Class<?>, Comparator<?>> classHandlers;
    private Type type;

    {
        customHandlers = new HashMap<>();
        fieldHandlers = new HashMap<>();
        classHandlers = new HashMap<>();
        type = Type.FIRST_RECORDED;
    }

    public Options() {
    }

    public Options(Type type) {
        this.type = type;
    }

    public <T> void addCustomHandler(String handlerName, Comparator<T> comparator) {
        customHandlers.put(handlerName, comparator);
    }

    public <T> void addClassHandler(Class<T> classType, Comparator<T> comparator) {
        classHandlers.put(classType, comparator);
    }

    public <T> void addFieldHandler(String fieldName, Comparator<T> comparator) {
        fieldHandlers.put(fieldName, comparator);
    }

    public enum Type {
        DO_NOTHING,
        FIRST_RECORDED,
        LAST_RECORDED,
    }

    public Type getType() {
        return type;
    }

    public Map<String, Comparator<?>> getCustomHandlers() {
        return customHandlers;
    }

    public Map<Class<?>, Comparator<?>> getClassHandlers() {
        return classHandlers;
    }

    public Map<String, Comparator<?>> getFieldHandlers() {
        return fieldHandlers;
    }

    /*public enum VarType {
        BYTE,
        SHORT,
        INT,
        LONG,
        FLOAT,
        DOUBLE,
        CHAR,
        BOOLEAN
    }*/
}
