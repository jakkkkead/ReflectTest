package com.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//元注解
// RetentionPolicy 对应3个阶段：SOURCE，CLASS，RUNTIME
// java源文件 --》java。class文件 --》内存中的字节 码
@Retention(RetentionPolicy.RUNTIME)
//注解可以使用的位置，包，方法，类（type），字段，构造函数等
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface TestAnnotation {
}
