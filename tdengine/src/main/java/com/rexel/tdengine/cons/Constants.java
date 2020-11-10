package com.rexel.tdengine.cons;

/**
 * @ClassName Constants
 * @Description Constants
 * @Author: chunhui.qu
 * @Date: 2020/10/13
 */
public class Constants {
    public static String TABLE = "table";
    public static String TIME = "time";
    public static String TAG = "tag";
    public static String VALUE = "value";

    public static String CREATE_TABLE = "create table if not exists {table} ({field}) tags ({tag});";
    public static String INSERT_INTO = "insert into ";
    public static String INSERT_BODY = "{table} ({field}) values ({value})";

    // 插入一条记录，数据对应到指定的列
    // INSERT INTO tb_name (field1_name, ...) VALUES(field1_value, ...)
    public static String INSERT_ONE = INSERT_INTO + INSERT_BODY;
    // 自动建表
    // INSERT INTO d1001 USING METERS TAGS ("Beijng.Chaoyang", 2) VALUES (now, 10.2, 219, 0.32);
    public static String INSERT_ONE_USING_SUPER = INSERT_INTO + "{table} using {superTable} tags ({tag}) values ({value})";
    // 同时向多个表按列插入多条记录
    // INSERT INTO
    // tb1_name (tb1_field1_name, ...) VALUES (field1_value1, ...) (field1_value2, ...)
    // tb2_name (tb2_field1_name, ...) VALUES (field1_value1, ...) (field1_value2, ...);
}
