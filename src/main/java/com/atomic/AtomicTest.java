package com.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 原子操作，多线程共同操作同一变量时使用。
 * 可以操作一个基本类型，数组内的基本类型，一个对象中的基本类型
 */
public class AtomicTest {
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static void main(String[] args) {
        int x = atomicInteger.addAndGet(4);
        int y = atomicInteger.decrementAndGet();
        int c = atomicInteger.incrementAndGet();
        /**
         * 参考网址：https://blog.csdn.net/u010412719/article/details/52068888
         * 1:字段必须用 volatile 修饰
         * 2：字段 只能是实例变量，不能是类变量，也就是说不能加static关键字，也不能用final修饰。
         * 3:字段的描述类型（修饰符public/protected/default/private）是与调用者与操作对象字段的关系一致。
         *   也就是说调用者能够直接操作对象字段，那么就可以反射进行原子操作。但是对于父类的字段，子类是不能直接操作的，尽管子类可以访问父类的字段。
         * 4:对于AtomicIntegerFieldUpdater和AtomicLongFieldUpdater只能修改int/long类型的字段，不能修改其包装类型（Integer/Long）。
         *    如果要修改包装类型就需要使用AtomicReferenceFieldUpdater。
         */
        AtomicIntegerFieldUpdater updater = AtomicIntegerFieldUpdater.newUpdater(TestClass.class,"age");
        int age = updater.addAndGet(new TestClass(),3);
        System.out.println("x:"+x+",y:"+y+",c:"+c+",age:"+age);

    }
}
class TestClass{
    // 用 volatile 修饰
    protected volatile int age;
    public TestClass(){}

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
