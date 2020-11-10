package rexel.com.dview.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import rexel.com.dview.utils.ParamUtils;

/**
 * @ClassName: LongGetVarValue
 * @Description: [2].通过变量索引读取变量值, 或某段时间内发生变化的变量值（用于长连接）
 * @Author: chunhui.qu@rexel.com.cn
 * @Date: 2020/2/21
 */
@Slf4j
public class LongGetVarValue extends AbstractGetVarValue {
    private Socket socket;

    /**
     * 构造函数
     *
     * @param socket Socket对象
     */
    public LongGetVarValue(Socket socket) {
        this.socket = socket;
    }

    /**
     * 执行逻辑
     *
     * @param varType    变量类型（16进制）
     * @param startIndex 开始索引（0-800000）
     * @param varCount   变量数量（0:全部,1-800000个）
     * @param timeRange  秒时间段（0:全部,1-3600秒）
     * @return 变量值Map
     */
    public Map<Integer, Object> execute(
            byte varType, int startIndex, int varCount, int timeRange) {
        log.debug("LongGetVarValue execute start. varType=" + varType);

        try {
            // 生成请求参数
            byte[] param = ParamUtils.makeParam2(varType, startIndex, varCount, timeRange);
            if (param == null || param.length == 0) {
                log.error("param error. varType=" + varType);
                return null;
            }

            // 发送命令
            OutputStream out = socket.getOutputStream();
            if (out == null) {
                log.error("OutputStream == null");
                return null;
            }
            out.write(param);
            out.flush();

            // 接收数据
            InputStream in = socket.getInputStream();
            if (in == null) {
                log.error("InputStream == null");
                return null;
            }

            // 解析数据头
            if (!checkHeaderSuccess(in)) {
                log.error("headLogic error.");
                return null;
            }

            // 解析数据体
            return bodyLogic(in, varType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.debug("LongGetVarValue execute end. varType=" + varType);
        return null;
    }

    /**
     * 检查功能码
     */
    @Override
    public boolean checkResponseFuncCode(byte b1, byte b2) {
        return b1 != 0x27 || b2 != 0x1C;
    }
}