package com.genericity;

import java.util.ArrayList;
import java.util.Vector;

/**
 * 泛型测试
 * 1.在编译阶段生效，在运行阶段，泛型擦除。
 * 2.泛型 只能用于引用类型，不能用基本类型
 */
public class GenericityTest {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("kdfj");
        //不能直接这样赋值，编译器会报错
       // ArrayList<Object> list1 = list;
        //list.add(2);
        ArrayList<?> list2 = new ArrayList();
        //不能将？赋值给 具体对象
    //    ArrayList<Object> list3 = list2;
        list2 = new ArrayList<Object>();
       //限定通配符的上边界 。 必须是 Number 或者 其子类
        ArrayList<? extends Number> list4 = new ArrayList<Integer>();
        //限定通配符的下边界，必须是Integer的父类 或者Integer
        ArrayList<? super Integer> list5 = new ArrayList<Number>();
        String [] a1 = new String[]{"aa","bb","cc"};
        swap(a1,0,1);
        // 泛型 只能使用引用类型，不能用基本类型
        int[] a2 = new int[]{1,2,3};
       // swap(a2,0,1);
        //不能使用泛型数组
     //   Vector<String>[] vector = new Vector<String>[10];

    }
    public static <T>void swap(T[]base , int i , int j){
        T tem = base[i];
        base[i] = base[j];
        base[j] = tem;
    }
}
