package ru.mrdux.interflower.objects;

import java.util.HashMap;
import java.util.Map;

public class Options {

    private Map<String, MergeHandler> customHandlers;
    private Map<String, MergeHandler> fieldHandlers;
    private Map<Class, MergeHandler> classHandlers;
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

    public <T> void addCustomHandler(String handlerName, MergeHandler<T> handler) {
        customHandlers.put(handlerName, handler);
    }

    public <T> void addClassHandler(Class<T> classType, MergeHandler<T> handler) {
        classHandlers.put(classType, handler);
    }

    public <T> void addFieldHandler(String fieldName, MergeHandler<T> handler) {
        fieldHandlers.put(fieldName, handler);
    }

    public enum Type {
        DO_NOTHING,
        FIRST_RECORDED,
        LAST_RECORDED,
    }

    public Type getType() {
        return type;
    }

    public Map<String, MergeHandler> getCustomHandlers() {
        return customHandlers;
    }

    public Map<String, MergeHandler> getFieldHandlers() {
        return fieldHandlers;
    }

    public Map<Class, MergeHandler> getClassHandlers() {
        return classHandlers;
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
