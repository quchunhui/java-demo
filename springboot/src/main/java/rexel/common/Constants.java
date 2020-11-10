package rexel.common;

public class Constants {
    /**
     * API接口
     */
    //获取访问令牌
    public static String OPEN_API_GET_TOKEN = "/openapi/token";
    //检测服务器连接
    public static String OPEN_API_GET_CHECK = "/openapi/check";
    //获取待加工数据
    public static String OPEN_API_GET_QUERY = "/openapi/ipm/wait/process/query";
    //回传加工状态
    public static String OPEN_API_POST_STATE = "/openapi/device/process/state";
    //上报设备状态（可选）
    public static String OPEN_API_POST_NOTIFY = "/openapi/device/event/notify";

    /**
     * 公共错误码
     */
    //成功
    public static int RESPONSE_OK = 200;
    //客户端错误：请检查请求参数是否正确
    public static int RESPONSE_CLIENT_ERR = 400;
    //无效令牌：请“获取访问令牌”后使用新令牌调用接口
    public static int RESPONSE_TOKEN_INVALID = 401;
    //没有权限：请确实是否拥有该接口调用权限
    public static int RESPONSE_LIMIT_AUTH  = 403;
    //接口不存在：请检查调用地址是否正确，或联系平台核实接口是否可用
    public static int RESPONSE_API_NOT_FOUNT = 404;
    //服务端错误：请联系平台处理
    public static int RESPONSE_SERVER_ERR = 500;

    /**
     * 加工状态（0:待加工、1：加工中、2：已完成）
     */
    public static int PROCESS_STATUS_TODO = 0;
    public static int PROCESS_STATUS_DOING = 1;
    public static int PROCESS_STATUS_DONE = 2;

    /**
     * REXEL用户ID
     */
    public static String REXEL_USER_ID = "rexel";

    /**
     * 测试JSON数据
     */
    public static String TEST_JSON =
        "{"
        + "\"code\": 200,"
        + "\"msg\": \"请求成功\","
        + "\"data\": [{"
        + "\"aluGrade\": 1100,"
        + "\"aluState\": \"H24\","
        + "\"thickness\": 2.10,"
        + "\"width\": 1200,"
        + "\"length\": 3000,"
        + "\"isFilm\": 0,"
        + "\"cnt\": 10,"
        + "\"id\": 10"
        + "}, {"
        + "\"aluGrade\": 1100,"
        + "\"aluState\": \"H24\","
        + "\"thickness\": 2.10,"
        + "\"width\": 1200,"
        + "\"length\": 3000,"
        + "\"isFilm\": 0,"
        + "\"cnt\": 10,"
        + "\"id\": 10"
        + "}]"
        + "}";
}
