package org.example.pojo.db;

import lombok.Data;

import java.util.List;

/**
 * org.example.pojo.db
 * User: fd
 * Date: 2024/08/07 23:34
 * Description:
 * Version: V1.0
 */
@Data
public class CompareResult {
    private List<ColumnDiff> columnDiffs;
    private List<TableDiff> tableDiffs;
}
