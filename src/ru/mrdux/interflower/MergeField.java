package ru.mrdux.interflower;

import java.lang.annotation.*;

@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MergeField {
    String handlerName() default "";
}