package com.enums;

/**
 * 测试枚举
 * 用与：使用的值在确定范围内，可以在编译阶段就确定值
 */
public class EnumTest {
    public static void main(String[] args) {
        EnumWeek enumWeek = EnumWeek.FRI;
      //  EnumWeek e = new Enum<EnumWeek>("SUM",0);
        System.out.println(enumWeek);
        System.out.println(enumWeek.ordinal());
        System.out.println(EnumWeek.valueOf("SAT"));
        System.out.println(EnumWeek.values().length);
        TraficLamp traficLamp = TraficLamp.RED;
        EnumWeek enumWeek1 = EnumWeek.SUM;
        enumWeek1.getNext();
    }
    public enum TraficLamp{
        //表明 会自动选择 哪个构造方法
        RED(1),GREEN(2),YELLOW(3);
        //所有的方法，字段必须放在 后面
        //构造方法只能是私有的
        private TraficLamp(){}
        private TraficLamp(int i){
            System.out.println("i:"+i+",value:"+this.name());
        }
    }

}
 enum EnumWeek{
    //选择无参构造,内部类的运用，实现了抽象方法，相当与是一个子类
    SUM(2){
       public EnumWeek getNext(){
           System.out.println("enumWeek:"+getTime());
            return EnumWeek.MON;
        }
    },
     MON(1){
         public EnumWeek getNext(){
             return EnumWeek.FRI;
         }
     },FRI{
         public EnumWeek getNext(){
             return EnumWeek.SAT;
         }
     },
     SAT{
         public EnumWeek getNext(){
             return EnumWeek.SUM;
         }
     };
    public abstract EnumWeek getNext();
    private int time;
    //有参构造
    private EnumWeek(int time){this.time = time;}
    private EnumWeek(){}
    public int getTime(){
        return time;
    }
}
