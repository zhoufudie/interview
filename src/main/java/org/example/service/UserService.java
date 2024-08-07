package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.pojo.entity.Admin;

/**
 * org.example.service
 * User: fd
 * Date: 2024/08/07 22:35
 * Description:
 * Version: V1.0
 */
public interface UserService extends IService<Admin> {

    String loginAdmin(String username, String password);


}
