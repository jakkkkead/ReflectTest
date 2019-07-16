package com.autopack;

/**
 * 测试自动包装
 * 参考网址：https://www.cnblogs.com/wang-yaz/p/8516151.html
 * 1、需要知道什么时候会引发装箱和拆箱,如遇到 基础运算符
 * 2、装箱操作会创建对象，频繁的装箱操作会消耗许多内存，影响性能，所以可以避免装箱的时候应该尽量避免。
 *
 * 3、equals(Object o) 因为原equals方法中的参数类型是封装类型，所传入的参数类型（a）是原始数据类型，所以会自动对其装箱，反之，会对其进行拆箱
 *
 * 4、当两种不同类型用==比较时，包装器类的需要拆箱， 当同种类型用==比较时，会自动拆箱或者装箱
 */
public class AutoPack {
    public static void main(String[] args) {
        //自动装箱,实际上调用了 Integer.valueOf();
        Integer x = 4;
        Integer b = 4;
        int w = 4;
        //享元模式，共用同一份数据。
        System.out.println("x==b:"+(x==b));//true
        System.out.println("x==w:"+(x==4));//true
        //自动拆箱,实际上调用了 Integer.intValue();
        System.out.println("x+w:"+x+w);
        //这里用new 的形式创建对象，引用会变得不一样
        Integer a = new Integer(3);
        Integer c = new Integer(3);
        System.out.println("a==c:"+(a==c));//false
        //先判断 是否 在-128<= i <= 127 之间，如果是，返回缓存中的数据，否则 返回 new Integer（i）
        Integer va = Integer.valueOf(5);
        Integer sa = Integer.valueOf(5);
        System.out.println("va==sa:"+(va==sa));//true
        //数据必须在一个字节内：-128~127 之内，才能共享同一份数据。
        Integer e = 147;
        Integer ew = 147;
        System.out.println("va==ew:"+(e==ew));//false
        System.out.println("va==ew:"+(e==147));//true
        Double d1 = 12.0;
        Double d2 = 12.0;
        //每次 return new Double(d);
        Double d3 = Double.valueOf(12.0);
        System.out.println("d1==d2:"+(d1==d2));//false
        System.out.println("d1==d3:"+(d1==d3));//false

    }

}
