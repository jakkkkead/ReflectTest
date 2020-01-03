package com.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    /**
     * 获取当前时间
     * 返回：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getNowDate(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
