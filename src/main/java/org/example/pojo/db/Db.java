package org.example.pojo.db;

import lombok.Data;

/**
 * org.example.pojo.db
 * User: fd
 * Date: 2024/08/07 23:35
 * Description:
 * Version: V1.0
 */
@Data
public class Db {
    // 数据库的名字
    private String name;
    private String url;
    private String username;
    private String password;
}
