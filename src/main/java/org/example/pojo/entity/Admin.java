package org.example.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * com.gxa.op.pojo.entity
 * User: fd
 * Date: 2024/06/12 15:22
 * Description:
 * Version: V1.0
 */
@Data
@TableName("op_admin")
public class Admin implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String face;
    private String image;
    private String salt;

    private String role;
    private Date created;
    private Date updated;

}
