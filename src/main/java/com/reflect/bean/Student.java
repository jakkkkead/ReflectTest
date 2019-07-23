package com.reflect.bean;

import java.util.Date;

public class Student {
    public String name;
    private int age;
    protected String teacherName;
    public int sex;
    private Date date = new Date();
    //public final String school = "天津";
    public final static String school = new String("天津");
    public String getSchool() {
        return school;
    }

    private Student(String name, int age , int sex, String teacherName){
        this.name = name;
        this.teacherName = teacherName;
        this.age = age;
        this.sex = sex;
    }
    public Student(String name){
        this.name = name;
    }
    public Student(){

    }

    @Override
    public String toString() {
       return "name:"+this.name+",techarcerName:"+teacherName+",age:"+age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public int getSex() {
        return sex;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
