package com.annotation;
@TestAnnotation
//给属性赋值
@TestAnnotation2(annotation = @TestAnnotation,color = "red",element={2,4})
public class AnnotationTest1 {
    public static void main(String[] args) {
        if(AnnotationTest1.class.isAnnotationPresent(TestAnnotation.class)){
            TestAnnotation testAnnotation = AnnotationTest1.class.getAnnotation(TestAnnotation.class);
            System.out.println(testAnnotation);
        }
        if(AnnotationTest1.class.isAnnotationPresent(TestAnnotation2.class)){
            TestAnnotation2 testAnnotation = AnnotationTest1.class.getAnnotation(TestAnnotation2.class);
            String color = testAnnotation.color();
            String value = testAnnotation.value();
            int[] elements = testAnnotation.element();
            System.out.println("color:"+color+",value:"+value);
            System.out.println("elements:"+elements[0]);
        }
    }
}
