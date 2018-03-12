package ru.mrdux.interflower.collections;

import java.util.List;

public class Context<CC, C> {

    private CC container;
    private C processedContainer;
    private List<C> mergedContainers;

    public Context(CC container, C processedContainer, List<C> mergedContainers) {
        this.container = container;
        this.processedContainer = processedContainer;
        this.mergedContainers = mergedContainers;
    }

    public CC getContainer() {
        return container;
    }

    public C getProcessedContainer() {
        return processedContainer;
    }

    public List<C> getMergedContainers() {
        return mergedContainers;
    }
}

