package net.common;

import java.text.SimpleDateFormat;
import java.util.Date;



public class DateUtil {

    /**
     * 返回字符串形式的当前日期
     * @Author AZhen
     * @Param [pattern]  模板参数  如："yyyy-MM-dd"
     * @return java.lang.String
     **/


    public static String getCurrentDateStr(String pattern){
        SimpleDateFormat format=new SimpleDateFormat(pattern);
        return format.format(new Date());
    }

    public static String getDateStr(String pattern,Date date){
        SimpleDateFormat format=new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
