package org.example.utils;

import org.example.pojo.db.ColumnDiff;
import org.example.pojo.db.CompareResult;
import org.example.pojo.db.Db;
import org.example.pojo.db.TableDiff;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * org.example.utils
 * User: fd
 * Date: 2024/08/07 23:32
 * Description:
 * Version: V1.0
 */
public class DbComparator {
    public CompareResult compare(Db db1, Db db2) throws SQLException {
        Map<String, List<ColumnInfo>> db1Schema = getSchema(db1);
        Map<String, List<ColumnInfo>> db2Schema = getSchema(db2);

        CompareResult result = new CompareResult();
        result.setTableDiffs(new ArrayList<>());
        result.setColumnDiffs(new ArrayList<>());

        // Compare tables
        for (String table : db1Schema.keySet()) {
            if (db2Schema.containsKey(table)) {
                // Both databases have the table, compare columns
                TableDiff tableDiff = new TableDiff();
                tableDiff.setName(table);

                List<ColumnDiff> columnDiffs = compareColumns(table, db1Schema.get(table), db2Schema.get(table));
                if (columnDiffs.isEmpty()) {
                    tableDiff.setDiff(1); // Tables are identical
                } else {
                    tableDiff.setDiff(2); // Tables have differences
                    result.getColumnDiffs().addAll(columnDiffs);
                }

                result.getTableDiffs().add(tableDiff);
            } else {
                // db1 has the table, db2 does not
                TableDiff tableDiff = new TableDiff();
                tableDiff.setName(table);
                tableDiff.setDiff(3);
                result.getTableDiffs().add(tableDiff);
            }
        }

        for (String table : db2Schema.keySet()) {
            if (!db1Schema.containsKey(table)) {
                // db2 has the table, db1 does not
                TableDiff tableDiff = new TableDiff();
                tableDiff.setName(table);
                tableDiff.setDiff(4);
                result.getTableDiffs().add(tableDiff);
            }
        }

        return result;
    }

    private Map<String, List<ColumnInfo>> getSchema(Db db) throws SQLException {
        Map<String, List<ColumnInfo>> schema = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword())) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    List<ColumnInfo> columns = new ArrayList<>();
                    try (ResultSet columnsRs = metaData.getColumns(null, null, tableName, "%")) {
                        while (columnsRs.next()) {
                            ColumnInfo column = new ColumnInfo();
                            column.setName(columnsRs.getString("COLUMN_NAME"));
                            column.setType(columnsRs.getString("TYPE_NAME"));
                            column.setSize(columnsRs.getInt("COLUMN_SIZE"));
                            column.setNullable(columnsRs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                            column.setDefaultValue(columnsRs.getString("COLUMN_DEF"));
                            column.setPrimaryKey(isPrimaryKey(metaData, tableName, column.getName()));
                            column.setAutoIncrement("YES".equals(columnsRs.getString("IS_AUTOINCREMENT")));
                            columns.add(column);
                        }
                    }
                    schema.put(tableName, columns);
                }
            }
        }
        return schema;
    }

    private boolean isPrimaryKey(DatabaseMetaData metaData, String tableName, String columnName) throws SQLException {
        try (ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName)) {
            while (primaryKeys.next()) {
                if (columnName.equals(primaryKeys.getString("COLUMN_NAME"))) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<ColumnDiff> compareColumns(String table, List<ColumnInfo> columns1, List<ColumnInfo> columns2) {
        List<ColumnDiff> columnDiffs = new ArrayList<>();
        Map<String, ColumnInfo> columns2Map = new HashMap<>();
        for (ColumnInfo column : columns2) {
            columns2Map.put(column.getName(), column);
        }

        for (ColumnInfo column1 : columns1) {
            ColumnInfo column2 = columns2Map.get(column1.getName());
            ColumnDiff columnDiff = new ColumnDiff();
            columnDiff.setTable(table);
            columnDiff.setName(column1.getName());

            if (column2 == null) {
                columnDiff.setDiff(3); // Column is in db1 but not in db2
                columnDiffs.add(columnDiff);
            } else {
                if (!column1.equals(column2)) {
                    columnDiff.setDiff(2); // Columns exist in both but have differences
                    columnDiffs.add(columnDiff);
                }
                columns2Map.remove(column1.getName());
            }
        }

        for (ColumnInfo column2 : columns2Map.values()) {
            ColumnDiff columnDiff = new ColumnDiff();
            columnDiff.setTable(table);
            columnDiff.setName(column2.getName());
            columnDiff.setDiff(4); // Column is in db2 but not in db1
            columnDiffs.add(columnDiff);
        }

        return columnDiffs;
    }

    // ColumnInfo class to store column details
    private static class ColumnInfo {
        private String name;
        private String type;
        private int size;
        private boolean nullable;
        private String defaultValue;
        private boolean primaryKey;
        private boolean autoIncrement;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public boolean isNullable() {
            return nullable;
        }

        public void setNullable(boolean nullable) {
            this.nullable = nullable;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public boolean isPrimaryKey() {
            return primaryKey;
        }

        public void setPrimaryKey(boolean primaryKey) {
            this.primaryKey = primaryKey;
        }

        public boolean isAutoIncrement() {
            return autoIncrement;
        }

        public void setAutoIncrement(boolean autoIncrement) {
            this.autoIncrement = autoIncrement;
        }
    }
}
