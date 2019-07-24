package com.genericity;

/**
 * 泛型方法，如果多个方法使用需要使用同一个泛型，则将泛型 加在 类上
 */
public class GenerityDao<T> {
    public void update(T obj){

    }
    public <T> T getDataById(String id){
        return null;
    }

    public void add(T obj){

    }
    //无法在静态方法上 使用 自定义泛型
//    public static void testStatic(T obj){
//
//    }
}
