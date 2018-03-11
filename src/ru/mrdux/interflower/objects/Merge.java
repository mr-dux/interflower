package ru.mrdux.interflower.objects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*TODO
* do one array with boolean field ignore=true\false
* */

@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Merge {
    String handlerName() default "";

    String[] mergeFields() default {};

    String[] ignoreField() default {};

}