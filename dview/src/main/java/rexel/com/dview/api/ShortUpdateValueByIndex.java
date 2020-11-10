package rexel.com.dview.api;

import com.alibaba.fastjson.JSONObject;
import rexel.com.dview.cons.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import rexel.com.dview.utils.ByteUtils;
import rexel.com.dview.utils.ParamUtils;
import rexel.com.dview.utils.SocketUtils;

/**
 * @ClassName: ShortUpdateValueByIndex
 * @Description: [4].通过变量索引, 批量修改变量值
 * @Author: chunhui.qu@rexel.com.cn
 * @Date: 2020/2/21
 */
@Slf4j
public class ShortUpdateValueByIndex extends AbstractBase {
    private String ipAddress;
    private int port;

    /**
     * 构造函数
     *
     * @param ipAddress ip地址
     * @param port      端口号
     */
    public ShortUpdateValueByIndex(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    /**
     * 执行逻辑
     *
     * @param varType   变量类型（16进制）
     * @param valueList 变量值列表
     */
    public JSONObject execute(byte varType, List<Object> valueList) {
        log.debug("ShortUpdateValueByIndex execute start. varType=" + varType);

        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            // 初始化Socket
            socket = new Socket(this.ipAddress, this.port);

            // 生成请求参数
            byte[] param = ParamUtils.makeParam4(varType, valueList);
            if (param == null) {
                log.error("param error. varType=" + varType);
                return null;
            }
            log.info("param" + Arrays.toString(param));

            // 发送命令
            out = socket.getOutputStream();
            if (out == null) {
                log.error("OutputStream == null");
                return null;
            }
            out.write(param);
            out.flush();

            // 接收数据
            in = socket.getInputStream();
            if (in == null) {
                log.error("InputStream == null");
                return null;
            }

            // 应答数据
            return myHeadLogic(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        log.debug("ShortUpdateValueByIndex execute end. varType=" + varType);
        return null;
    }

    /**
     * 检查服务器应答消息头
     *
     * @param in 消息输入流
     * @return fale：失败、true：成功
     * @throws IOException e
     */
    private JSONObject myHeadLogic(InputStream in) throws IOException {
        byte[] header = new byte[10];
        int len = SocketUtils.read(in, header);
        log.debug("header=" + Arrays.toString(header));
        if (len != header.length) {
            log.error("The returned byte length is incorrect;");
            return null;
        }

        //检查应答标识
        //Buffer[0][1]
        if (checkResponseIdent(header[0], header[1])) {
            log.error("checkResponseIdent error.");
        }

        //检查功能码
        //Buffer[2][3]
        if (checkResponseFuncCode(header[2], header[3])) {
            log.error("checkResponseFuncCode error.");
        }

        //检查变量类型
        //Buffer[4]
        if (checkResponseVarType(header[4])) {
            log.info("checkResponseVarType error.");
        }

        //检查应答结果是否正确
        //Buffer[5]
        byte result = header[5];
        if (checkResponseResult(result)) {
            log.error("checkResponseResult error. result=" + Constants.DVIEW_MSG_MAP.get((int) result));
        }

        // 成功计数
        // Buffer[6][7]
        byte[] okBuf = new byte[2];
        System.arraycopy(header, 6, okBuf, 0, okBuf.length);
        int okCount = ByteUtils.bytesToInt(okBuf);
        log.info("succeed count:" + okCount);

        // 失败计数
        // Buffer[8][9]
        byte[] ngBuf = new byte[2];
        System.arraycopy(header, 8, ngBuf, 0, ngBuf.length);
        int ngCount = ByteUtils.bytesToInt(ngBuf);
        log.info("failed count:" + ngCount);

        JSONObject resultJson = new JSONObject();
        resultJson.put(Constants.DVIEW_REQUEST_ID, String.valueOf(header[0]) + String.valueOf(header[1]));
        resultJson.put(Constants.DVIEW_METHOD, String.valueOf(header[2]) + String.valueOf(header[3]));
        resultJson.put(Constants.DVIEW_VARIATE_TYPE, header[4]);
        resultJson.put(Constants.DVIEW_STATUS_CODE, header[5]);
        resultJson.put(Constants.DVIEW_OK_COUNT, okCount);
        resultJson.put(Constants.DVIEW_NG_COUNT, ngCount);

        return resultJson;
    }

    /**
     * 检查功能码
     */
    @Override
    public boolean checkResponseFuncCode(byte b1, byte b2) {
        return b1 != 0x27 || b2 != 0x1D;
    }
}
