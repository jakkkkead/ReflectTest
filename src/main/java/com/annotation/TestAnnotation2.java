package com.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 给注解添加属性
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface TestAnnotation2 {
    /**
     * 颜色
     * @return
     */
    String color();

    /**
     * value,默认324
     * @return
     */
    String value() default "324";
    int [] element();
    TestAnnotation annotation() default @TestAnnotation;
}
