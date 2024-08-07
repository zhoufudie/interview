package org.example.permission;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.example.utils.TokenUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * com.gxa.op.utils
 * User: fd
 * Date: 2024/06/15 11:44
 * Description:鉴权--验证token并保存信息到当前线程
 * Version: V1.0
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //1.获取token
        //请求头
        String token = request.getHeader("token");
        if(token.isEmpty()){
            //请求体
            token = request.getParameter("token");
            if(token.isEmpty()){
                return false;
            }
        }

        //获取到之后判断合法性
        if(!JWTUtil.verify(token, TokenUtils.KEY.getBytes())){
            return false;
        }

        //获得合法token后判断是否失效
        //从当前线程中获取载荷
        JWT jwt = JWTUtil.parseToken(token);
        //获取有效时间
        NumberWithFormat expire = (NumberWithFormat)jwt.getPayload("expire");
        if(System.currentTimeMillis()>expire.longValue()){
            return false;
        }

        //未失效就可以获取载荷数据
        Map<String,Object> map = new HashMap();
        map.put("id",jwt.getPayload("id"));
        map.put("name",jwt.getPayload("name"));
        map.put("username",jwt.getPayload("username"));
        map.put("image",jwt.getPayload("image"));
        map.put("phone",jwt.getPayload("phone"));
        map.put("role",jwt.getPayload("role"));

        //把信息放到当前线程
        AdminThreadLocal.set(map);

        return true ;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
