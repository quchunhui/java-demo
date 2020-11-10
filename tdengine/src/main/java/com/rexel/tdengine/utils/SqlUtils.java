package com.rexel.tdengine.utils;

import com.rexel.tdengine.cons.SqlConstants;
import com.rexel.tdengine.pojo.PointInfo;
import java.util.List;
import java.util.Random;

/**
 * @ClassName SqlSpeller
 * @Description SqlSpeller（单列模式）
 * @Author: chunhui.qu
 * @Date: 2020/11/11
 */
public class SqlUtils {
    /**
     * 生成SQL语句
     * create table if not exists {table} ({field}) tags ({tag});
     *
     * @param pointInfo PointInfo
     * @return SQL语句
     */
    public static String getCreateSuperTableSql(PointInfo pointInfo) {
        String sql = SqlConstants.CREATE_SUPER_TABLE;
        sql = sql.replace(SqlConstants.PARAM_SUPER_TABLE, pointInfo.getSuperTable());
        sql = sql.replace(SqlConstants.PARAM_FIELDS, "time timestamp, value double");
        sql = sql.replace(SqlConstants.PARAM_TAGS, AnnotationUtils.getTagString(pointInfo));
        return sql + ";";
    }

    /**
     * 生成SQL语句
     *
     * @param pointInfoList List<PointInfo>
     * @return SQL语句
     */
    public static String insertBatchUsingSuper(List<PointInfo> pointInfoList) {
        StringBuilder sql = new StringBuilder(SqlConstants.INSERT_INTO_HEAD);
        for (PointInfo pointInfo : pointInfoList) {
            String sysTime = CommonUtils.timeLongToStr(System.currentTimeMillis());
            String values = "\"" + sysTime + "\"" + "," + new Random().nextInt(10000);
            String sqlBody = SqlConstants.INSERT_INTO_BODY;
            sqlBody = sqlBody.replace(SqlConstants.PARAM_TABLE, pointInfo.getTableName());
            sqlBody = sqlBody.replace(SqlConstants.PARAM_SUPER_TABLE, pointInfo.getSuperTable());
            sqlBody = sqlBody.replace(SqlConstants.PARAM_TAGS, AnnotationUtils.getTagValueString(pointInfo));
            sqlBody = sqlBody.replace(SqlConstants.PARAM_VALUES, values);
            sql.append(sqlBody).append(" ");
        }
        sql.append(";");
        return sql.toString();
    }
}