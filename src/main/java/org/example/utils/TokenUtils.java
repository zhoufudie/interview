package org.example.utils;

import cn.hutool.jwt.JWTUtil;

import java.util.Map;

/**
 * com.gxa.com.utils
 * User: fd
 * Date: 2024/06/15 10:57
 * Description:生成token令牌
 * Version: V1.0
 */
public class TokenUtils {

    public static final String KEY="sdfsdfdsafdsgfdhgbfdhfdghafaslasfsafcaasfsafnrtjhafsef";

    /**
     * 创建Jwt令牌
     * @param payload
     * @return
     */
    public static String createToken(Map<String,Object> payload){
        return JWTUtil.createToken(payload,KEY.getBytes());
    }


}
