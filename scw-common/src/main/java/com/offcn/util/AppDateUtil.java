package com.offcn.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppDateUtil {
    public static String getFormatTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(new Date());
        return format;
    }
    public static String getFormatTime(String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String format = sdf.format(new Date());
        return format;
    }
    public static String getFormatTime(String pattern, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String string = format.format(date);
        return string;
    }
}
