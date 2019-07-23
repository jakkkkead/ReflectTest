package com.reflect.Test;

import com.reflect.bean.Student;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * 内省操作
 * Java语言对Bean类属性、事件的一种缺省处理方法。例如类A中有属性name,那我们可以通过getName,setName来得到其值或者设置新的值。
 * 通过getName/setName来访问name属性，这就是默认的规则。
 * Java中提供了一套API用来访问某个属性的getter/setter方法，
 * 通过这些API可以使你不需要了解这个规则（但你最好还是要搞清楚），这些API存放于包java.beans中。
 *
 * 一般的做法是通过类Introspector来获取某个对象的BeanInfo信息，
 * 然后通过BeanInfo来获取属性的描述器（PropertyDescriptor），
 * 通过这个属性描述器就可以获取某个属性对应的getter/setter方法，
 * 然后我们就可以通过反射机制来调用这些方法。
 */
public class BeanInfoTest {
    public static void main(String[] args) throws Exception {
        Student student = new Student("丹迪");
        String propertyName = "name";
        BeanInfoTest.getProperty(student,propertyName);
        BeanInfoTest.setProperty(student,propertyName,"dkfskal");
      //  write.invoke(student,1);
        System.out.println("name的读方法："+student.getName());
    }

    public static void getProperty(Object object , String propertyName) throws Exception{
        //这里可以用反射 进行获取方法，但是操作麻烦。直接用内省
        PropertyDescriptor descriptor = new PropertyDescriptor(propertyName,object.getClass());
        //获取的方法不能是private
        Method read = descriptor.getReadMethod();
        System.out.println("name的读方法："+read.invoke(object));
    }
    public static void setProperty(Object object,String propertyName,String val) throws Exception{
        PropertyDescriptor descriptor = new PropertyDescriptor(propertyName,object.getClass());
        System.out.println("描述的属性："+descriptor.getName());
        Method write = descriptor.getWriteMethod();
        //方法设置 必须传递相应类型的参数
        write.invoke(object,val);

    }
    public static void getBeanUserInfo(Object object,String propertyName) throws Exception{
       BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
       PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
       //循环找到对应的属性
       for (int i =0; i< propertyDescriptors.length;i++){
           if(propertyDescriptors[i].getName().equals(propertyName)){
              Method method = propertyDescriptors[i].getReadMethod();
              if(!method.isAccessible()) {
                  method.setAccessible(true);
              }
              Object val =method.invoke(object);
              System.out.println("propertyName的get方法："+val);
              break;
           }
       }

    }

}
