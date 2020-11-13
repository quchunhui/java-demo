package com.rexel.tdengine.api;

import com.rexel.tdengine.utils.TdUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CreateContinuousQuery
 * @Description CreateContinuousQuery
 * @Author: chunhui.qu
 * @Date: 2020/11/13
 */
public class CreateContinuousQuery {
    private static TdUtils tdUtils = TdUtils.getInstance();
    private static Statement statement;
    private final static String PRODUCT_KEY = "a1B6t6ZG6oR";
    private final static String DEVICE_NAME = "RexelLabDevice1";
    private final static String DATABASE_FROM = "rexel_online";
    private final static String DATABASE_TO = "rexel_online_analysis";

    public static void main(String[] args) throws SQLException {
        System.out.println("CreateContinuousQuery Start.");
        Connection conn = tdUtils.getConnection();
        if (conn == null) {
            return;
        }
        System.out.println("get connection");

        statement = conn.createStatement();
        if (statement == null) {
            return;
        }
        System.out.println("createStatement");

        List<String> pointIdList = getPointIdList();
        List<String> timeBlockList = getTimeBlockList();

        for (String pointId : pointIdList) {
            for (String timeBlock : timeBlockList) {
                createContinuousQuery(pointId, timeBlock);
                System.out.println("cq is created. pointId=" + pointId + ", timeBlock=" + timeBlock);
            }
        }

        System.out.println("CreateContinuousQuery End.");
    }

    private static void createContinuousQuery(String pointId, String timeBlock)
        throws SQLException {
        // create table cq_test.product1_device1_ai_point1_sum as
        // select sum(value) as sum, avg(value) as avg from stress_test.st_device_data_up interval(1m);
        String fromTable = PRODUCT_KEY + "_" + DEVICE_NAME + "_" + pointId;
        String toTable = fromTable + "_" + timeBlock;

        StringBuilder sb = new StringBuilder();
        sb.append(" create table ");
        sb.append(DATABASE_TO).append(".").append(toTable);
        sb.append(" as select ");
        sb.append(getFieldString());
        sb.append(" from ");
        sb.append(DATABASE_FROM).append(".").append(fromTable);
        sb.append(" interval(").append(timeBlock).append(")");
        sb.append(";");

        statement.executeQuery(sb.toString());
    }

    private static String getFieldString() {
        List<String[]> functionList = getFunctionList();
        StringBuilder sb = new StringBuilder();
        for (String[] strings : functionList) {
            String function = strings[0];
            String as = strings[1];
            sb.append(function).append("(value)").append(" as ").append(as).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }


    private static List<String> getTimeBlockList() {
        return new ArrayList<String>(){{
            add("5m");
            add("10m");
            add("30m");
            add("1h");
            add("6h");
            add("12h");
            add("1d");
        }};
    }

    private static List<String[]> getFunctionList() {
        return new ArrayList<String[]>(){{
            add(new String[]{"first", "first"});
            add(new String[]{"last", "last"});
            add(new String[]{"max", "max"});
            add(new String[]{"min", "min"});
            add(new String[]{"avg", "mean"});
            add(new String[]{"spread", "spread"});
            add(new String[]{"sum", "sum"});
        }};
    }

    private static List<String> getPointIdList() {
        return new ArrayList<String>(){{
            add("AI01_0001");
            add("AI01_0002");
            add("AI01_0003");
            add("AI01_0004");
            add("AI01_0005");
            add("AI01_0006");
            add("AI01_0007");
            add("AI01_0008");
            add("AI01_0009");
            add("AI01_0010");
            add("AI01_0011");
            add("AI01_0012");
            add("AI01_0013");
            add("AI01_0014");
            add("AI01_0015");
            add("AI01_0016");
            add("AI01_0017");
            add("AI01_0018");
            add("AI01_0019");
            add("AI01_0020");
            add("AI01_0021");
            add("AI01_0022");
            add("AI01_0023");
            add("AI01_0024");
            add("AI01_0025");
            add("AI01_0026");
            add("AI01_0027");
            add("AI01_0028");
            add("AI01_0029");
            add("AI01_0030");
            add("AI01_0031");
            add("AI01_0032");
            add("AI01_0033");
            add("AI01_0034");
            add("AI01_0035");
            add("AI01_0036");
            add("AI01_0037");
            add("AI01_0038");
            add("AI01_0039");
            add("AI01_0040");
            add("AI01_0041");
            add("AI01_0042");
            add("AI01_0043");
            add("AI01_0044");
            add("AI01_0045");
            add("AI01_0046");
            add("AI01_0047");
            add("AI01_0048");
            add("AI01_0049");
            add("AI01_0050");
            add("AI01_0051");
            add("AI01_0052");
            add("AI01_0053");
            add("AI01_0054");
            add("AI01_0055");
            add("AI01_0056");
            add("AI01_0057");
            add("AI01_0058");
            add("AI01_0059");
            add("AI01_0060");
            add("AI01_0061");
            add("AI01_0062");
            add("AI01_0063");
            add("AI01_0064");
            add("AI01_0065");
            add("AI01_0066");
            add("AI01_0067");
            add("AI01_0068");
            add("AI01_0069");
            add("AI01_0070");
            add("AI01_0071");
            add("AI01_0072");
            add("AI01_0073");
            add("AI01_0074");
            add("AI01_0075");
            add("AI01_0076");
            add("AI01_0077");
            add("AI01_0078");
            add("AI01_0079");
            add("AI01_0080");
            add("AI01_0081");
            add("AI01_0082");
            add("AI01_0083");
            add("AI01_0084");
            add("AI01_0085");
            add("AI01_0086");
            add("AI01_0087");
            add("AI01_0088");
            add("AI01_0089");
            add("AI01_0090");
            add("AI01_0091");
            add("AI01_0092");
            add("AI01_0093");
            add("AI01_0094");
            add("AI01_0095");
            add("AI01_0096");
            add("AI01_0097");
            add("AI01_0098");
            add("AI01_0099");
            add("AI01_0100");
            add("AI01_0101");
            add("AI01_0102");
            add("AI01_0103");
            add("AI01_0104");
            add("AI01_0105");
            add("AI01_0106");
            add("AI01_0107");
            add("AI01_0108");
            add("AI01_0109");
            add("AI01_0110");
            add("AI01_0111");
            add("AI01_0112");
            add("AI01_0113");
            add("AI01_0114");
            add("AI01_0115");
            add("AI01_0116");
            add("AI01_0117");
            add("AI01_0118");
            add("AI01_0119");
            add("AI01_0120");
            add("AI01_0121");
            add("AI01_0122");
            add("AI01_0123");
            add("AI01_0124");
            add("AI01_0125");
            add("AI01_0126");
            add("AI01_0127");
            add("AI01_0128");
            add("AI01_0129");
            add("AI01_0130");
            add("AI01_0131");
            add("AI01_0132");
            add("AI01_0133");
            add("AI01_0134");
            add("AI01_0135");
            add("AI01_0136");
            add("AI01_0137");
            add("AI01_0138");
            add("AI01_0139");
            add("AI01_0140");
            add("AI01_0141");
            add("AI01_0142");
            add("AI01_0143");
            add("AI01_0144");
            add("AI01_0145");
            add("AI01_0146");
            add("AI01_0147");
            add("AI01_0148");
            add("AI01_0149");
            add("AI01_0150");
            add("AI01_0151");
            add("AI01_0152");
            add("AI01_0153");
            add("AI01_0154");
            add("AI01_0155");
            add("AI01_0156");
            add("AI01_0157");
            add("AI01_0158");
            add("AI01_0159");
            add("AI01_0160");
            add("AI01_0161");
            add("AI01_0162");
            add("AI01_0163");
            add("AI01_0164");
            add("AI01_0165");
            add("AI01_0166");
            add("AI01_0167");
            add("AI01_0168");
            add("AI01_0169");
            add("AI01_0170");
            add("AI01_0171");
            add("AI01_0172");
            add("AI01_0173");
            add("AI01_0174");
            add("AI01_0175");
            add("AI01_0176");
            add("AI01_0177");
            add("AI01_0178");
            add("AI01_0179");
            add("AI01_0180");
            add("AI01_0181");
            add("AI01_0182");
            add("AI01_0183");
            add("AI01_0184");
            add("AI01_0185");
            add("AI01_0186");
            add("AI01_0187");
            add("AI01_0188");
            add("AI01_0189");
            add("AI01_0190");
            add("AI01_0191");
            add("AI01_0192");
            add("AI01_0193");
            add("AI01_0194");
            add("AI01_0195");
            add("AI01_0196");
            add("AI01_0197");
            add("AI01_0198");
            add("AI01_0199");
            add("AI01_0200");
            add("AI01_0201");
            add("AI01_0202");
            add("AI01_0203");
            add("AI01_0204");
            add("AI01_0205");
            add("AI01_0206");
            add("AI01_0207");
            add("AI01_0208");
            add("AI01_0209");
            add("AI01_0210");
            add("AI01_0211");
            add("AI01_0212");
            add("AI01_0213");
            add("AI01_0214");
            add("AI01_0215");
            add("AI01_0216");
            add("AI01_0217");
            add("AI01_0218");
            add("AI01_0219");
            add("AI01_0220");
            add("AI01_0221");
            add("AI01_0222");
            add("AI01_0223");
            add("AI01_0224");
            add("AI01_0225");
            add("AI01_0226");
            add("AI01_0227");
            add("AI01_0228");
            add("AI01_0229");
            add("AI01_0230");
            add("AI01_0231");
            add("AI01_0232");
            add("AI01_0233");
            add("AI01_0234");
            add("AI01_0235");
            add("AI01_0236");
            add("AI01_0237");
            add("AI01_0238");
            add("AI01_0239");
            add("AI01_0240");
            add("AI01_0241");
            add("AI01_0242");
            add("AI01_0243");
            add("AI01_0244");
            add("AI01_0245");
            add("AI01_0246");
            add("AI01_0247");
            add("AI01_0248");
            add("AI01_0249");
            add("AI01_0250");
            add("AI01_0251");
            add("AI01_0252");
            add("AI01_0253");
            add("AI01_0254");
            add("AI01_0255");
            add("AI01_0256");
            add("AI02_0001");
            add("AI02_0002");
            add("AI02_0003");
            add("AI02_0004");
            add("AI02_0005");
            add("AI02_0006");
            add("AI02_0007");
            add("AI02_0008");
            add("AI02_0009");
            add("AI02_0010");
            add("AI02_0011");
            add("AI02_0012");
            add("AI02_0013");
            add("AI02_0014");
            add("AI02_0015");
            add("AI02_0016");
            add("AI02_0017");
            add("AI02_0018");
            add("AI02_0019");
            add("AI02_0020");
            add("AI02_0021");
            add("AI02_0022");
            add("AI02_0023");
            add("AI02_0024");
            add("AI02_0025");
            add("AI02_0026");
            add("AI02_0027");
            add("AI02_0028");
            add("AI02_0029");
            add("AI02_0030");
            add("AI02_0031");
            add("AI02_0032");
            add("AI02_0033");
            add("AI02_0034");
            add("AI02_0035");
            add("AI02_0036");
            add("AI02_0037");
            add("AI02_0038");
            add("AI02_0039");
            add("AI02_0040");
            add("AI02_0041");
            add("AI02_0042");
            add("AI02_0043");
            add("AI02_0044");
            add("AI02_0045");
            add("AI02_0046");
            add("AI02_0047");
            add("AI02_0048");
            add("AI02_0049");
            add("AI02_0050");
            add("AI02_0051");
            add("AI02_0052");
            add("AI02_0053");
            add("AI02_0054");
            add("AI02_0055");
            add("AI02_0056");
            add("AI02_0057");
            add("AI02_0058");
            add("AI02_0059");
            add("AI02_0060");
            add("AI02_0061");
            add("AI02_0062");
            add("AI02_0063");
            add("AI02_0064");
            add("AI02_0065");
            add("AI02_0066");
            add("AI02_0067");
            add("AI02_0068");
            add("AI02_0069");
            add("AI02_0070");
            add("AI02_0071");
            add("AI02_0072");
            add("AI02_0073");
            add("AI02_0074");
            add("AI02_0075");
            add("AI02_0076");
            add("AI02_0077");
            add("AI02_0078");
            add("AI02_0079");
            add("AI02_0080");
            add("AI02_0081");
            add("AI02_0082");
            add("AI02_0083");
            add("AI02_0084");
            add("AI02_0085");
            add("AI02_0086");
            add("AI02_0087");
            add("AI02_0088");
            add("AI02_0089");
            add("AI02_0090");
            add("AI02_0091");
            add("AI02_0092");
            add("AI02_0093");
            add("AI02_0094");
            add("AI02_0095");
            add("AI02_0096");
            add("AI02_0097");
            add("AI02_0098");
            add("AI02_0099");
            add("AI02_0100");
            add("AI02_0101");
            add("AI02_0102");
            add("AI02_0103");
            add("AI02_0104");
            add("AI02_0105");
            add("AI02_0106");
            add("AI02_0107");
            add("AI02_0108");
            add("AI02_0109");
            add("AI02_0110");
            add("AI02_0111");
            add("AI02_0112");
            add("AI02_0113");
            add("AI02_0114");
            add("AI02_0115");
            add("AI02_0116");
            add("AI02_0117");
            add("AI02_0118");
            add("AI02_0119");
            add("AI02_0120");
            add("AI02_0121");
            add("AI02_0122");
            add("AI02_0123");
            add("AI02_0124");
            add("AI02_0125");
            add("AI02_0126");
            add("AI02_0127");
            add("AI02_0128");
            add("AI02_0129");
            add("AI02_0130");
            add("AI02_0131");
            add("AI02_0132");
            add("AI02_0133");
            add("AI02_0134");
            add("AI02_0135");
            add("AI02_0136");
            add("AI02_0137");
            add("AI02_0138");
            add("AI02_0139");
            add("AI02_0140");
            add("AI02_0141");
            add("AI02_0142");
            add("AI02_0143");
            add("AI02_0144");
            add("AI02_0145");
            add("AI02_0146");
            add("AI02_0147");
            add("AI02_0148");
            add("AI02_0149");
            add("AI02_0150");
            add("AI02_0151");
            add("AI02_0152");
            add("AI02_0153");
            add("AI02_0154");
            add("AI02_0155");
            add("AI02_0156");
            add("AI02_0157");
            add("AI02_0158");
            add("AI02_0159");
            add("AI02_0160");
            add("AI02_0161");
            add("AI02_0162");
            add("AI02_0163");
            add("AI02_0164");
            add("AI02_0165");
            add("AI02_0166");
            add("AI02_0167");
            add("AI02_0168");
            add("AI02_0169");
            add("AI02_0170");
            add("AI02_0171");
            add("AI02_0172");
            add("AI02_0173");
            add("AI02_0174");
            add("AI02_0175");
            add("AI02_0176");
            add("AI02_0177");
            add("AI02_0178");
            add("AI02_0179");
            add("AI02_0180");
            add("AI02_0181");
            add("AI02_0182");
            add("AI02_0183");
            add("AI02_0184");
            add("AI02_0185");
            add("AI02_0186");
            add("AI02_0187");
            add("AI02_0188");
            add("AI02_0189");
            add("AI02_0190");
            add("AI02_0191");
            add("AI02_0192");
            add("AI02_0193");
            add("AI02_0194");
            add("AI02_0195");
            add("AI02_0196");
            add("AI02_0197");
            add("AI02_0198");
            add("AI02_0199");
            add("AI02_0200");
            add("AI02_0201");
            add("AI02_0202");
            add("AI02_0203");
            add("AI02_0204");
            add("AI02_0205");
            add("AI02_0206");
            add("AI02_0207");
            add("AI02_0208");
            add("AI02_0209");
            add("AI02_0210");
            add("AI02_0211");
            add("AI02_0212");
            add("AI02_0213");
            add("AI02_0214");
            add("AI02_0215");
            add("AI02_0216");
            add("AI02_0217");
            add("AI02_0218");
            add("AI02_0219");
            add("AI02_0220");
            add("AI02_0221");
            add("AI02_0222");
            add("AI02_0223");
            add("AI02_0224");
            add("AI02_0225");
            add("AI02_0226");
            add("AI02_0227");
            add("AI02_0228");
            add("AI02_0229");
            add("AI02_0230");
            add("AI02_0231");
            add("AI02_0232");
            add("AI02_0233");
            add("AI02_0234");
            add("AI02_0235");
            add("AI02_0236");
            add("AI02_0237");
            add("AI02_0238");
            add("AI02_0239");
            add("AI02_0240");
            add("AI02_0241");
            add("AI02_0242");
            add("AI02_0243");
            add("AI02_0244");
            add("AI02_0245");
            add("AI02_0246");
            add("AI02_0247");
            add("AI02_0248");
            add("AI02_0249");
            add("AI02_0250");
            add("AI02_0251");
            add("AI02_0252");
            add("AI02_0253");
            add("AI02_0254");
            add("AI02_0255");
            add("AI02_0256");
            add("AI03_0001");
            add("AI03_0002");
            add("AI03_0003");
            add("AI03_0004");
            add("AI03_0005");
            add("AI03_0006");
            add("AI03_0007");
            add("AI03_0008");
            add("AI03_0009");
            add("AI03_0010");
            add("AI03_0011");
            add("AI03_0012");
            add("AI03_0013");
            add("AI03_0014");
            add("AI03_0015");
            add("AI03_0016");
            add("AI03_0017");
            add("AI03_0018");
            add("AI03_0019");
            add("AI03_0020");
            add("AI03_0021");
            add("AI03_0022");
            add("AI03_0023");
            add("AI03_0024");
            add("AI03_0025");
            add("AI03_0026");
            add("AI03_0027");
            add("AI03_0028");
            add("AI03_0029");
            add("AI03_0030");
            add("AI03_0031");
            add("AI03_0032");
            add("AI03_0033");
            add("AI03_0034");
            add("AI03_0035");
            add("AI03_0036");
            add("AI03_0037");
            add("AI03_0038");
            add("AI03_0039");
            add("AI03_0040");
            add("AI03_0041");
            add("AI03_0042");
            add("AI03_0043");
            add("AI03_0044");
            add("AI03_0045");
            add("AI03_0046");
            add("AI03_0047");
            add("AI03_0048");
            add("AI03_0049");
            add("AI03_0050");
            add("AI03_0051");
            add("AI03_0052");
            add("AI03_0053");
            add("AI03_0054");
            add("AI03_0055");
            add("AI03_0056");
            add("AI03_0057");
            add("AI03_0058");
            add("AI03_0059");
            add("AI03_0060");
            add("AI03_0061");
            add("AI03_0062");
            add("AI03_0063");
            add("AI03_0064");
            add("AI03_0065");
            add("AI03_0066");
            add("AI03_0067");
            add("AI03_0068");
            add("AI03_0069");
            add("AI03_0070");
            add("AI03_0071");
            add("AI03_0072");
            add("AI03_0073");
            add("AI03_0074");
            add("AI03_0075");
            add("AI03_0076");
            add("AI03_0077");
            add("AI03_0078");
            add("AI03_0079");
            add("AI03_0080");
            add("AI03_0081");
            add("AI03_0082");
            add("AI03_0083");
            add("AI03_0084");
            add("AI03_0085");
            add("AI03_0086");
            add("AI03_0087");
            add("AI03_0088");
            add("AI03_0089");
            add("AI03_0090");
            add("AI03_0091");
            add("AI03_0092");
            add("AI03_0093");
            add("AI03_0094");
            add("AI03_0095");
            add("AI03_0096");
            add("AI03_0097");
            add("AI03_0098");
            add("AI03_0099");
            add("AI03_0100");
            add("AI03_0101");
            add("AI03_0102");
            add("AI03_0103");
            add("AI03_0104");
            add("AI03_0105");
            add("AI03_0106");
            add("AI03_0107");
            add("AI03_0108");
            add("AI03_0109");
            add("AI03_0110");
            add("AI03_0111");
            add("AI03_0112");
            add("AI03_0113");
            add("AI03_0114");
            add("AI03_0115");
            add("AI03_0116");
            add("AI03_0117");
            add("AI03_0118");
            add("AI03_0119");
            add("AI03_0120");
            add("AI03_0121");
            add("AI03_0122");
            add("AI03_0123");
            add("AI03_0124");
            add("AI03_0125");
            add("AI03_0126");
            add("AI03_0127");
            add("AI03_0128");
            add("AI03_0129");
            add("AI03_0130");
            add("AI03_0131");
            add("AI03_0132");
            add("AI03_0133");
            add("AI03_0134");
            add("AI03_0135");
            add("AI03_0136");
            add("AI03_0137");
            add("AI03_0138");
            add("AI03_0139");
            add("AI03_0140");
            add("AI03_0141");
            add("AI03_0142");
            add("AI03_0143");
            add("AI03_0144");
            add("AI03_0145");
            add("AI03_0146");
            add("AI03_0147");
            add("AI03_0148");
            add("AI03_0149");
            add("AI03_0150");
            add("AI03_0151");
            add("AI03_0152");
            add("AI03_0153");
            add("AI03_0154");
            add("AI03_0155");
            add("AI03_0156");
            add("AI03_0157");
            add("AI03_0158");
            add("AI03_0159");
            add("AI03_0160");
            add("AI03_0161");
            add("AI03_0162");
            add("AI03_0163");
            add("AI03_0164");
            add("AI03_0165");
            add("AI03_0166");
            add("AI03_0167");
            add("AI03_0168");
            add("AI03_0169");
            add("AI03_0170");
            add("AI03_0171");
            add("AI03_0172");
            add("AI03_0173");
            add("AI03_0174");
            add("AI03_0175");
            add("AI03_0176");
            add("AI03_0177");
            add("AI03_0178");
            add("AI03_0179");
            add("AI03_0180");
            add("AI03_0181");
            add("AI03_0182");
            add("AI03_0183");
            add("AI03_0184");
            add("AI03_0185");
            add("AI03_0186");
            add("AI03_0187");
            add("AI03_0188");
            add("AI03_0189");
            add("AI03_0190");
            add("AI03_0191");
            add("AI03_0192");
            add("AI03_0193");
            add("AI03_0194");
            add("AI03_0195");
            add("AI03_0196");
            add("AI03_0197");
            add("AI03_0198");
            add("AI03_0199");
            add("AI03_0200");
            add("AI03_0201");
            add("AI03_0202");
            add("AI03_0203");
            add("AI03_0204");
            add("AI03_0205");
            add("AI03_0206");
            add("AI03_0207");
            add("AI03_0208");
            add("AI03_0209");
            add("AI03_0210");
            add("AI03_0211");
            add("AI03_0212");
            add("AI03_0213");
            add("AI03_0214");
            add("AI03_0215");
            add("AI03_0216");
            add("AI03_0217");
            add("AI03_0218");
            add("AI03_0219");
            add("AI03_0220");
            add("AI03_0221");
            add("AI03_0222");
            add("AI03_0223");
            add("AI03_0224");
            add("AI03_0225");
            add("AI03_0226");
            add("AI03_0227");
            add("AI03_0228");
            add("AI03_0229");
            add("AI03_0230");
            add("AI03_0231");
            add("AI03_0232");
            add("AI03_0233");
            add("AI03_0234");
            add("AI03_0235");
            add("AI03_0236");
            add("AI03_0237");
            add("AI03_0238");
            add("AI03_0239");
            add("AI03_0240");
            add("AI03_0241");
            add("AI03_0242");
            add("AI03_0243");
            add("AI03_0244");
            add("AI03_0245");
            add("AI03_0246");
            add("AI03_0247");
            add("AI03_0248");
            add("AI03_0249");
            add("AI03_0250");
            add("AI03_0251");
            add("AI03_0252");
            add("AI03_0253");
            add("AI03_0254");
            add("AI03_0255");
            add("AI03_0256");
            add("AI04_0001");
            add("AI04_0002");
            add("AI04_0003");
            add("AI04_0004");
            add("AI04_0005");
            add("AI04_0006");
            add("AI04_0007");
            add("AI04_0008");
            add("AI04_0009");
            add("AI04_0010");
            add("AI04_0011");
            add("AI04_0012");
            add("AI04_0013");
            add("AI04_0014");
            add("AI04_0015");
            add("AI04_0016");
            add("AI04_0017");
            add("AI04_0018");
            add("AI04_0019");
            add("AI04_0020");
            add("AI04_0021");
            add("AI04_0022");
            add("AI04_0023");
            add("AI04_0024");
            add("AI04_0025");
            add("AI04_0026");
            add("AI04_0027");
            add("AI04_0028");
            add("AI04_0029");
            add("AI04_0030");
            add("AI04_0031");
            add("AI04_0032");
            add("AI04_0033");
            add("AI04_0034");
            add("AI04_0035");
            add("AI04_0036");
            add("AI04_0037");
            add("AI04_0038");
            add("AI04_0039");
            add("AI04_0040");
            add("AI04_0041");
            add("AI04_0042");
            add("AI04_0043");
            add("AI04_0044");
            add("AI04_0045");
            add("AI04_0046");
            add("AI04_0047");
            add("AI04_0048");
            add("AI04_0049");
            add("AI04_0050");
            add("AI04_0051");
            add("AI04_0052");
            add("AI04_0053");
            add("AI04_0054");
            add("AI04_0055");
            add("AI04_0056");
            add("AI04_0057");
            add("AI04_0058");
            add("AI04_0059");
            add("AI04_0060");
            add("AI04_0061");
            add("AI04_0062");
            add("AI04_0063");
            add("AI04_0064");
            add("AI04_0065");
            add("AI04_0066");
            add("AI04_0067");
            add("AI04_0068");
            add("AI04_0069");
            add("AI04_0070");
            add("AI04_0071");
            add("AI04_0072");
            add("AI04_0073");
            add("AI04_0074");
            add("AI04_0075");
            add("AI04_0076");
            add("AI04_0077");
            add("AI04_0078");
            add("AI04_0079");
            add("AI04_0080");
            add("AI04_0081");
            add("AI04_0082");
            add("AI04_0083");
            add("AI04_0084");
            add("AI04_0085");
            add("AI04_0086");
            add("AI04_0087");
            add("AI04_0088");
            add("AI04_0089");
            add("AI04_0090");
            add("AI04_0091");
            add("AI04_0092");
            add("AI04_0093");
            add("AI04_0094");
            add("AI04_0095");
            add("AI04_0096");
            add("AI04_0097");
            add("AI04_0098");
            add("AI04_0099");
            add("AI04_0100");
            add("AI04_0101");
            add("AI04_0102");
            add("AI04_0103");
            add("AI04_0104");
            add("AI04_0105");
            add("AI04_0106");
            add("AI04_0107");
            add("AI04_0108");
            add("AI04_0109");
            add("AI04_0110");
            add("AI04_0111");
            add("AI04_0112");
            add("AI04_0113");
            add("AI04_0114");
            add("AI04_0115");
            add("AI04_0116");
            add("AI04_0117");
            add("AI04_0118");
            add("AI04_0119");
            add("AI04_0120");
            add("AI04_0121");
            add("AI04_0122");
            add("AI04_0123");
            add("AI04_0124");
            add("AI04_0125");
            add("AI04_0126");
            add("AI04_0127");
            add("AI04_0128");
            add("AI04_0129");
            add("AI04_0130");
            add("AI04_0131");
            add("AI04_0132");
            add("AI04_0133");
            add("AI04_0134");
            add("AI04_0135");
            add("AI04_0136");
            add("AI04_0137");
            add("AI04_0138");
            add("AI04_0139");
            add("AI04_0140");
            add("AI04_0141");
            add("AI04_0142");
            add("AI04_0143");
            add("AI04_0144");
            add("AI04_0145");
            add("AI04_0146");
            add("AI04_0147");
            add("AI04_0148");
            add("AI04_0149");
            add("AI04_0150");
            add("AI04_0151");
            add("AI04_0152");
            add("AI04_0153");
            add("AI04_0154");
            add("AI04_0155");
            add("AI04_0156");
            add("AI04_0157");
            add("AI04_0158");
            add("AI04_0159");
            add("AI04_0160");
            add("AI04_0161");
            add("AI04_0162");
            add("AI04_0163");
            add("AI04_0164");
            add("AI04_0165");
            add("AI04_0166");
            add("AI04_0167");
            add("AI04_0168");
            add("AI04_0169");
            add("AI04_0170");
            add("AI04_0171");
            add("AI04_0172");
            add("AI04_0173");
            add("AI04_0174");
            add("AI04_0175");
            add("AI04_0176");
            add("AI04_0177");
            add("AI04_0178");
            add("AI04_0179");
            add("AI04_0180");
            add("AI04_0181");
            add("AI04_0182");
            add("AI04_0183");
            add("AI04_0184");
            add("AI04_0185");
            add("AI04_0186");
            add("AI04_0187");
            add("AI04_0188");
            add("AI04_0189");
            add("AI04_0190");
            add("AI04_0191");
            add("AI04_0192");
            add("AI04_0193");
            add("AI04_0194");
            add("AI04_0195");
            add("AI04_0196");
            add("AI04_0197");
            add("AI04_0198");
            add("AI04_0199");
            add("AI04_0200");
            add("AI04_0201");
            add("AI04_0202");
            add("AI04_0203");
            add("AI04_0204");
            add("AI04_0205");
            add("AI04_0206");
            add("AI04_0207");
            add("AI04_0208");
            add("AI04_0209");
            add("AI04_0210");
            add("AI04_0211");
            add("AI04_0212");
            add("AI04_0213");
            add("AI04_0214");
            add("AI04_0215");
            add("AI04_0216");
            add("AI04_0217");
            add("AI04_0218");
            add("AI04_0219");
            add("AI04_0220");
            add("AI04_0221");
            add("AI04_0222");
            add("AI04_0223");
            add("AI04_0224");
            add("AI04_0225");
            add("AI04_0226");
            add("AI04_0227");
            add("AI04_0228");
            add("AI04_0229");
            add("AI04_0230");
            add("AI04_0231");
            add("AI04_0232");
            add("AI04_0233");
            add("AI04_0234");
            add("AI04_0235");
            add("AI04_0236");
            add("AI04_0237");
            add("AI04_0238");
            add("AI04_0239");
            add("AI04_0240");
            add("AI04_0241");
            add("AI04_0242");
            add("AI04_0243");
            add("AI04_0244");
            add("AI04_0245");
            add("AI04_0246");
            add("AI04_0247");
            add("AI04_0248");
            add("AI04_0249");
            add("AI04_0250");
            add("AI04_0251");
            add("AI04_0252");
            add("AI04_0253");
            add("AI04_0254");
            add("AI04_0255");
            add("AI04_0256");
        }};
    }
}
