package net.common;

import java.util.UUID;


public class IDUtil {

    /**
     * 获取uuid（以去掉'-'字符）
     * @Author AZhen
     * @Param []
     * @return java.lang.String
     **/
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}