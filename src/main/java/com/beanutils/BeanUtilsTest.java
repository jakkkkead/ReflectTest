package com.beanutils;

import com.reflect.bean.Student;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.HashMap;
import java.util.Map;

public class BeanUtilsTest {
    public static void main(String[] args) throws Exception {
        Student student = new Student("dklfj");
        //用BeanUtils设置属性值，不用严格匹配属性类型,会自动转换类型
        BeanUtils.setProperty(student,"name",3);
        //级联操作，date必须要初始化，否则报空指针异常
        BeanUtils.setProperty(student,"date.time",34234234);
        Map<String,String> map = new HashMap<String, String>();
        map.put("name","ldkfjlksaj");
        map.put("teacherName","lisi");
        BeanUtils.populate(student,map);
        System.out.println("更改后的student："+ student.toString());
        System.out.println("更改后的student的date"+ student.getDate());
        //用PropertyUtils 设置字段必须 使用符合字段类型的值
        PropertyUtils.setProperty(student,"name","里斯");
        System.out.println("更改后的student："+ student.toString());
    }
}
