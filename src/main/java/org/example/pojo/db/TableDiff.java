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
public class TableDiff {
    private String name;
    // 1=表都有并且完全相同, 2=表都有但是存在不同, 3=db1有 && db2没有, 4=db2有 && db1没有
    private int diff;
}
