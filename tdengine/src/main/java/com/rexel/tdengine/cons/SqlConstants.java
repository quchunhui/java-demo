package com.rexel.tdengine.cons;

/**
 * @ClassName Constants
 * @Description Constants
 * @Author: chunhui.qu
 * @Date: 2020/10/13
 */
public class SqlConstants {
    public final static String PARAM_DATABASE = "{database}";
    public final static String PARAM_TABLE = "{table}";
    public final static String PARAM_SUPER_TABLE = "{superTable}";
    public final static String PARAM_FIELDS = "{fields}";
    public final static String PARAM_TAGS = "{tags}";
    public final static String PARAM_VALUES = "{values}";

    public final static String CREATE_DATABASE = " create database if not exists {database} keep 3650 days 5 blocks 12 ";
    public final static String CREATE_SUPER_TABLE = " create table if not exists {superTable} ({fields}) tags ({tags}) ";
    public final static String INSERT_INTO_HEAD = " insert into ";
    public final static String INSERT_INTO_BODY = " {table} using {superTable} tags ({tags}) values ({values})";
}
