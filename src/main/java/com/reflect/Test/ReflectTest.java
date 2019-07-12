package com.reflect.Test;

import com.reflect.bean.Student;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 测试反射代码打法
 */
public class ReflectTest {
    public static void main(String[] args) throws Exception {
        Class student = Student.class;

        Constructor[] constructors = student.getDeclaredConstructors();
        System.out.println("=========获取到的所有构造方法（不包括父类）===============");
        for (Constructor c : constructors){
            System.out.println(c);
        }
        //获取指定参数的构造函数
        Constructor simpleContructor = student.getDeclaredConstructor(String.class);
        if (!simpleContructor.isAccessible()){
            simpleContructor.setAccessible(true);
        }
        //根据构造函数创建实例对象
        Student instance = (Student)simpleContructor.newInstance("小明");
        getFiledValue(instance,"name");
//        System.out.println("初次实例化对象，名字为："+instan);
        Method[] methods = student.getMethods();
        System.out.println("=========获取到的public方法（包括父类）===============");
        for (Method method : methods){
        /*    JAVA 反射机制中，Field的getModifiers()方法返回int类型值表示该字段的修饰符。
            其中，该修饰符是java.lang.reflect.Modifier的静态属性。
            对应表如下：
            PUBLIC: 1
            PRIVATE: 2
            PROTECTED: 4
            STATIC: 8
            FINAL: 16
            SYNCHRONIZED: 32
            VOLATILE: 64
            TRANSIENT: 128
            NATIVE: 256
            INTERFACE: 512
            ABSTRACT: 1024
            STRICT: 2048*/
            System.out.println("modifier:"+ method.getModifiers());
            System.out.println(method);


        }
        System.out.println("=========获取到的所有方法（比包括父类）===============");
        Method[] allMethods = student.getDeclaredMethods();
//            for (Method m : allMethods){
//                System.out.println(m);
//            }
        //获取指定方法名的方法
        Method detailMethod = student.getDeclaredMethod("setName",String.class);
        detailMethod.invoke(instance,"天亮了");
        System.out.println("重新设置name");
        getFiledValue(instance,"name");

        Method getMethod = student.getDeclaredMethod("getAge");
        Object age =getMethod.invoke(instance);
        System.out.println(age);
        //设置某个字段的值都是
        setFiledValue(instance,"teacherName","无良");
        System.out.println("teacherName："+instance.getTeacherName());
        setFinalField();
    }

    /**
     * 测试，重置final类型的属性
     *  final 与static 不可混为一谈，static 只是该类的字段在内存中只有一份（可以正常反射）
     *
     *  误区: 就是总以为加了 final 关键值，该属性就会被编译器内联优化，就不能用反射有效的进行修改。
     *  通过今天的梳理终于清晰的明白了：final 属性，只要不是基本类型和字面 String，就可以正常使用反射修改它的值。
     *  所以对于基本类型和字面 String, final 只是预示着它的值不能被正常的代码修改。
     * @throws Exception
     */
    public static void setFinalField() throws Exception {
        Student student = new Student();
        Class c = student.getClass();
        System.out.println("==================重置常量属性==================");
        System.out.println("school："+student.getSchool());
        Field sFiled = c.getDeclaredField("school");
        //直接开放 为true 无效
//        sFiled.setAccessible(true);
//        sFiled.set(student,"河阳");
//        System.out.println("改变后访问权限后，school:"+student.getSchool());
        Field modifier = Field.class.getDeclaredField("modifiers");
        System.out.println("modifier:"+Modifier.toString(sFiled.getModifiers()));
        modifier.setAccessible(true);
        //需要更改final的限制
        modifier.setInt(sFiled,sFiled.getModifiers()&~Modifier.FINAL);
        sFiled.set(student,"越南");
        Object val = sFiled.get(student);
        System.out.println("直接利用反射获取school字段："+val);
        /*
          这里输出的还是没有改变的值
          原因：因为JVM在编译时期, 就把final类型的String进行了优化, 在编译时期就会把String处理成常量，调用getSchool()时固定返回原有值
          解决方案：public final String school = new String("天津")
         */
        System.out.println("改变后，school:"+student.getSchool());
    }


    public static void getFiledValue(Object object , String fieldName){
        Class c = object.getClass();
        Field field = null;
        Object value = null;
        try {
            field = c.getDeclaredField(fieldName);
            if(!field.isAccessible()){
                field.setAccessible(true);
                //获取实例对象的 字段值
                value = field.get(object);
                //设置实例对象的字段值
               // field.set(object,"记得");
                field.setAccessible(false);
            }else {
                value = field.get(object);
            }

         System.out.println("对象的字段："+fieldName+"对应的值为："+value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setFiledValue(Object object , String fieldName,Object val){
        Class c = object.getClass();
        Field field = null;
        try {
            field = c.getDeclaredField(fieldName);
            if(!field.isAccessible()){
                field.setAccessible(true);
                //设置实例对象的字段值
                 field.set(object,val);
                field.setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
