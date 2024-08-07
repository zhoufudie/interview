package org.example.permission;

import java.util.Map;

/**
 * com.gxa.op.utils
 * User: fd
 * Date: 2024/06/15 11:17
 * Description:编写当前线程--存储认证过后的载荷
 * Version: V1.0
 */
public class AdminThreadLocal {

    private static final ThreadLocal<Map<String,Object>> tl=new ThreadLocal<>();

    /**
     * 绑定用户到当前线程
     * @param map
     */
    public static void set(Map<String,Object> map){
        tl.set(map);
    }

    /**
     * 获取当前线程绑定的用户信息
     * @return
     */
    public static Map<String,Object> get(){
        return tl.get();
    }

    /**
     * 移除当前线程绑定的用户信息
     */
    public static void remove(){
        tl.remove();
    }
}
