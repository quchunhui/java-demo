package rexel.com.dview.api;

import rexel.com.dview.cons.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import rexel.com.dview.utils.AnalysisUtils;
import rexel.com.dview.utils.SocketUtils;

/**
 * @ClassName: DViewBase
 * @Description: DView接口父类
 * @Author: chunhui.qu@rexel.com.cn
 * @Date: 2020/2/21
 */
@Data
@Slf4j
abstract class AbstractBase {
    /**
     * 检查服务器应答消息头
     *
     * @param in 消息输入流
     * @return fale：失败、true：成功
     * @throws IOException e
     */
    boolean checkHeaderSuccess(InputStream in) throws IOException {
        byte[] header = new byte[6];
        int bytes = SocketUtils.read(in, header);
        log.debug("header=" + Arrays.toString(header));
        if (bytes != header.length) {
            log.error("The returned byte length is incorrect;");
            return false;
        }

        // 检查应答标识 Buffer[0][1]
        if (checkResponseIdent(header[0], header[1])) {
            log.error("checkResponseIdent error.");
            return false;
        }

        // 检查功能码 Buffer[2][3]
        if (checkResponseFuncCode(header[2], header[3])) {
            log.error("checkResponseFuncCode error.");
            return false;
        }

        // 检查变量类型 Buffer[4]
        if (checkResponseVarType(header[4])) {
            log.info("checkResponseVarType error.");
            return false;
        }

        // 检查应答结果是否正确 Buffer[5]
        byte result = header[5];
        if (checkResponseResult(result)) {
            log.error("checkResponseResult error. result=" + Constants.DVIEW_MSG_MAP.get((int) result));
            return false;
        }

        return true;
    }

    /**
     * 检查应答标识
     * @param b1 Buffer[0]
     * @param b2 Buffer[1]
     * @return 结果
     */
    boolean checkResponseIdent(byte b1, byte b2) {
        return b1 != 0x3C || b2 != 0x2A;
    }

    /**
     * 检查功能码
     * @param b1 Buffer[2]
     * @param b2 Buffer[3]
     * @return 结果
     */
    abstract boolean checkResponseFuncCode(byte b1, byte b2);

    /**
     * 检查变量类型
     * @param b Buffer[4]
     * @return 结果
     */
    boolean checkResponseVarType(byte b) {
        return null == AnalysisUtils.getVarTypeCategory(b);
    }

    /**
     * 检查状态代码
     * @param b Buffer[5]
     * @return 结果
     */
    boolean checkResponseResult(byte b) {
        return (b != 0x00 && b != 0x02);
    }
}