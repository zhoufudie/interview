package org.example.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mapper.UserMapper;
import org.example.pojo.entity.Admin;
import org.example.service.UserService;
import org.example.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * org.example.service.impl
 * User: fd
 * Date: 2024/08/07 22:35
 * Description:
 * Version: V1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,Admin> implements UserService {

    @Autowired
    private PermissionService permissionService;

    @Override
    public String loginAdmin(String username, String password) {
        //获取用户名,判断是否存在
        //1.判空
        if(username.isEmpty() || password.isEmpty()){
            return "用户名与密码不能为空";
        }
        //2.判断用户名
        LambdaQueryWrapper<Admin> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Admin::getUsername,username);
        Admin admin = getBaseMapper().selectOne(lambdaQueryWrapper);
        if(admin==null){
            return "该用户不存在";
        }

        //3.获取盐对用户输入的密码进行加密
        String salt = admin.getSalt();
        String md5Password = admin.getPassword();

        //4.对比两次密码是否一致
        String psw = DigestUtil.md5Hex(password + salt);
        if(!psw.equals(md5Password)){
            return "用户名或密码不对";
        }

        //优化权限控制---获取用户的角色
        String role = admin.getRole();
        //permissions.stream().map()

        //5.保存信息生成token
        Map<String, Object> map = new HashMap();
        map.put("id",admin.getId());
        map.put("name",admin.getName());
        map.put("username",admin.getUsername());
        map.put("image",admin.getImage());
        map.put("phone",admin.getPhone());
        map.put("expire",System.currentTimeMillis()+1000*60*60*2);
        map.put("role", role);

        String token = TokenUtils.createToken(map);

        //6.返回token
        return token;
    }


}
