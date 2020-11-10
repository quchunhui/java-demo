package rexel.com.dview.api;

import rexel.com.dview.cons.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import rexel.com.dview.utils.ByteUtils;
import rexel.com.dview.utils.ParamUtils;
import rexel.com.dview.utils.SocketUtils;

/**
 * @ClassName: ShortGetVarNameAndIndex
 * @Description: 1.获取某类型变量名称及变量索引
 * @Author: chunhui.qu@rexel.com.cn
 * @Date: 2020/2/21
 */
@Slf4j
public class ShortGetVarNameAndIndex extends AbstractBase {
    private String ipAddress;
    private int port;
    private int bodyLength = 0;

    /**
     * 构造函数
     *
     * @param ipAddress ip地址
     * @param port      端口号
     */
    public ShortGetVarNameAndIndex(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    /**
     * 执行逻辑
     *
     * @param varType    变量类型（16进制）
     * @param startIndex 开始索引（0-800000）
     * @param varCount   变量数量（0:全部,1-800000个）
     * @return 请求结果数据
     */
    public Map<Integer, String> execute(byte varType, int startIndex, int varCount) {
        log.debug("ShortGetVarNameAndIndex execute start. varType=" + varType);

        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            // 生成请求参数
            byte[] param = ParamUtils.makeParam1(varType, startIndex, varCount);
            if (param == null) {
                log.error("param error. varType=" + varType);
                return null;
            }

            // 初始化Socket
            socket = new Socket(this.ipAddress, this.port);

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

            // 解析数据头
            if (!checkHeaderSuccess(in)) {
                log.error("headLogic error.");
                return null;
            }

            // 解析数据体
            return bodyLogic(in);
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

        log.debug("ShortGetVarNameAndIndex execute end. varType=" + varType);
        return null;
    }

    /**
     * 检查服务器应答消息头
     *
     * @param in 消息输入流
     * @return fale：失败、true：成功
     * @throws IOException e
     */
    @Override
    boolean checkHeaderSuccess(InputStream in) throws IOException {
        byte[] header = new byte[9];
        int len = SocketUtils.read(in, header);
        log.debug("header=" + Arrays.toString(header));
        if (len != header.length) {
            log.error("The returned byte length is incorrect;");
            return false;
        }

        //检查应答标识
        //Buffer[0][1]
        if (checkResponseIdent(header[0], header[1])) {
            log.error("checkResponseIdent error.");
            return false;
        }

        //检查功能码
        //Buffer[2][3]
        if (checkResponseFuncCode(header[2], header[3])) {
            log.error("checkResponseFuncCode error.");
            return false;
        }

        //检查变量类型
        //Buffer[4]
        if (checkResponseVarType(header[4])) {
            log.error("checkResponseVarType error.");
            return false;
        }

        //检查应答结果是否正确
        //Buffer[5]
        byte result = header[5];
        if (checkResponseResult(result)) {
            log.error("checkResponseResult error. result=" + result);
            return false;
        }

        //获取数据包长度
        //Buffer[6][7][8]
        bodyLength =  ByteUtils.bytesToInt(Arrays.copyOfRange(header, 6, 9));

        return true;
    }

    /**
     * 数据体解析处理
     *
     * @param in 输入流
     * @return MapKey：变量索引、MapValue：变量名称
     * @throws IOException e
     */
    private Map<Integer, String> bodyLogic(InputStream in) throws IOException {
        Map<Integer, String> result = new HashMap<>(100);
        if (bodyLength <= 0) {
            return result;
        }

        for(;;) {
            // 获取单条数据长度
            byte[] lenBuf = new byte[1];
            int bytes = SocketUtils.read(in, lenBuf);
            if (bytes != lenBuf.length) {
                log.error("The returned byte length is incorrect;");
                return null;
            }
            int len = ByteUtils.bytesToInt(lenBuf);

            // 读取结束
            if (len <= 0) {
                return result;
            }

            // 获取单条数据索引
            byte[] indexBuf = new byte[3];
            bytes = SocketUtils.read(in, indexBuf);
            if (bytes != indexBuf.length) {
                log.error("The returned byte length is incorrect;");
                return null;
            }
            int index = ByteUtils.bytesToInt(indexBuf);

            // 获取单条数据值
            byte[] valueBuf = new byte[len - indexBuf.length];
            bytes = SocketUtils.read(in, valueBuf);
            if (bytes != valueBuf.length) {
                log.error("The returned byte length is incorrect;");
                return null;
            }
            String bodyStr = ByteUtils.bytesToStr(
                    valueBuf, Charset.forName(Constants.DVIEW_CHARSET_GBK));

            // 保存至Map
            result.put(index, bodyStr);
        }
    }

    /**
     * 检查功能码
     */
    @Override
    public boolean checkResponseFuncCode(byte b1, byte b2) {
        return b1 != 0x27 || b2 != 0x1B;
    }
}
