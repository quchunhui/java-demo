package rexel.com.dview.cons;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: Constants
 * @Description: 静态常量管理类
 * @Author: chunhui.qu@rexel.com.cn
 * @Date: 2020/2/21
 */
public class Constants {
    /**
     * DView API错误码
     */
    public static Map<Integer, String> DVIEW_MSG_MAP = new HashMap<Integer, String>() {{
        put(0x00, "无错误");
        put(0x01, "变量类型不正确");
        put(0x02, "变量实际数量零");
        put(0x03, "变量开始索引错误");
        put(0x04, "读取变量数量错误");
        put(0x05, "禁止修改");
        put(0x06, "修改变量全部失败");
        put(0x07, "修改变量部分失败");
        put(0x08, "批量变量名称格式错误");
        put(0xFE, "加密狗授权不支持此协议");
        put(0xFF, "访问运行数据库失败");
    }};

    /**
     * 最多变量索引数
     */
    public static int LIST_SPLIT_LEN = 2730;
    /**
     * 数据包最大长度
     */
    public static int DATA_PACKAGE_MAX = 8192;
    /**
     * 开始索引字节数
     */
    public static int INDEX_BYTE_LEN = 3;
    /**
     * 变量数量字节数
     */
    public static int COUNT_BYTE_LEN = 3;
    /**
     * 秒时间段字节数
     */
    public static int RANGE_BYTE_LEN = 2;
    /**
     * 多变量名分隔符号
     */
    public static String VAR_NAME_DIVIDE_MARK = "|";

    /**
     * 变量类型:模拟(R8): AI=01,AO=02,AR=03,VA=07
     */
    public static String VAR_CATEGORY_R8 = "R8";
    /**
     * 变量类型:模拟(R4): AI=11,AO=12,AR=13,VA=17
     */
    public static String VAR_CATEGORY_R4 = "R4";
    /**
     * 变量类型:开关(I1): DI=04,DO=05,DR=06,VD=08
     */
    public static String VAR_CATEGORY_I1 = "I1";
    /**
     * 变量类型:文本(ST): VT=09
     */
    public static String VAR_CATEGORY_ST = "ST";

    /**
     * DView API变量类型
     */
    public static Map<String, Byte> VAR_TYPE_MAP = new HashMap<String, Byte>() {{
        put("AI", (byte) 0x01);
        put("AO", (byte) 0x02);
        put("AR", (byte) 0x03);
        put("DI", (byte) 0x04);
        put("DO", (byte) 0x05);
        put("DR", (byte) 0x06);
        put("VA", (byte) 0x07);
        put("VD", (byte) 0x08);
        put("VT", (byte) 0x09);
    }};

    /**
     * DView字符编码
     */
    public static String DVIEW_CHARSET_GBK = "GBK";

    /**
     * 应答Json字段
     */
    public static String DVIEW_REQUEST_ID = "requestId";
    public static String DVIEW_METHOD = "method";
    public static String DVIEW_VARIATE_TYPE = "variateType";
    public static String DVIEW_STATUS_CODE = "statusCode";
    public static String DVIEW_OK_COUNT = "okCount";
    public static String DVIEW_NG_COUNT = "ngCount";
}