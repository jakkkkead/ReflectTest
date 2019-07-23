package com.valuetransfer;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试引用传递
 */
public class ValueTransferTest {
    public static void main(String[] args) {
       List<String> list = new ArrayList<String>();
       list.add("kdjf");
       list.add("skdjfkjf");
       System.out.println(list);
       //传递的是存储单元中的内容（副本），而不是存储单元的引用！
        handlerObj(list);
        System.out.println(list);
    }
    public static void handlerObj(List<String> list){
        List<String> test = new ArrayList<String>();
        test.add("星座书上");
        // list 的 引用指向了另一个 存储单元，但是对于调用者来说无影响
        list = test;
        System.out.println("方法中的list"+list);
    }
}
