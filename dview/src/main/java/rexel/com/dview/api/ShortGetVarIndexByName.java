package rexel.com.dview.api;

import rexel.com.dview.cons.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import rexel.com.dview.pojo.DViewVarInfo;
import rexel.com.dview.utils.AnalysisUtils;
import rexel.com.dview.utils.ByteUtils;
import rexel.com.dview.utils.ParamUtils;
import rexel.com.dview.utils.SocketUtils;

/**
 * @ClassName: ShortGetVarIndexByName
 * @Description: [5].得到某批变量名称对应索引
 * @Author: chunhui.qu@rexel.com.cn
 * @Date: 2020/2/21
 */
@Slf4j
public class ShortGetVarIndexByName extends AbstractBase {
    private String ipAddress;
    private int port;

    /**
     * 构造函数
     *
     * @param ipAddress ip地址
     * @param port      端口号
     */
    public ShortGetVarIndexByName(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    /**
     * 执行逻辑
     *
     * @param varType  变量类型（16进制）
     * @param nameList 变量名称列表
     * @return 请求结果数据
     */
    public List<DViewVarInfo> execute(byte varType, List<String> nameList) {
        log.debug("ShortGetVarIndexByName execute start. varType=" + varType);

        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            // 生成请求参数
            byte[] param = ParamUtils.makeParam5(varType, nameList);
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
            List<DViewVarInfo> resultList = bodyLogic(in);
            if (resultList != null) {
                log.debug("resultList.size=" + resultList.size());
            }
            return resultList;
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

        log.debug("ShortGetVarIndexByName execute end. varType=" + varType);
        return null;
    }

    /**
     * 数据体解析处理
     *
     * @param in 输入流
     * @return 请求结果数据
     * @throws IOException e
     */
    private List<DViewVarInfo> bodyLogic(InputStream in) throws IOException {
        List<DViewVarInfo> result = new ArrayList<>();

        // 获取数据包长度
        byte[] lenBuf = new byte[2];
        int bytes = SocketUtils.read(in, lenBuf);
        if (bytes != lenBuf.length) {
            log.error("The returned byte length is incorrect;");
            return null;
        }
        int len = ByteUtils.bytesToInt(lenBuf);

        // 检查是否超长
        if (len <= 0 || len > Constants.DATA_PACKAGE_MAX) {
            log.error("the body length is over to " + Constants.DATA_PACKAGE_MAX + ".");
            return null;
        }

        // 获取数据包
        byte[] bodyBuf = new byte[len];
        bytes = SocketUtils.read(in, bodyBuf);
        if (bytes != bodyBuf.length) {
            log.error("The returned byte length is incorrect;");
            return null;
        }

        // 将解析数据包解析为对象
        int position = 0;
        while (position < len) {
            DViewVarInfo bean = new DViewVarInfo();
            position += AnalysisUtils.analysisIndex(bodyBuf, position, bean);
            result.add(bean);
        }

        log.info("message count:" + result.size());
        return result;
    }

    /**
     * 检查功能码
     */
    @Override
    public boolean checkResponseFuncCode(byte b1, byte b2) {
        return b1 != 0x27 || b2 != 0x1F;
    }
}