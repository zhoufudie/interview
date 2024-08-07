package org.example.pojo.db;

import lombok.Data;

/**
 * org.example.pojo.db
 * User: fd
 * Date: 2024/08/07 23:34
 * Description:
 * Version: V1.0
 */
@Data
public class ColumnDiff {
    private String table;
    private String name;
    // 1=字段都有并且完全相同, 2=字段都有但是存在不同, 3=db1有 && db2没有, 4=db2有 && db1没有
    // 字段不同指：字段类型不同、字段长度不同、字段是否为空不同、字段默认值不同、
    // 字段是否为主键不同、字段是否自增不同
    private int diff;
}
