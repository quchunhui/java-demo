package rexel.com.dview.api;

import rexel.com.dview.cons.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import rexel.com.dview.pojo.DViewVarInfo;
import rexel.com.dview.utils.AnalysisUtils;
import rexel.com.dview.utils.ByteUtils;
import rexel.com.dview.utils.SocketUtils;

/**
 * @ClassName: GetVarValueBase
 * @Description: [通过变量索引读取变量值]接口的所属父类
 * @Author: chunhui.qu@rexel.com.cn
 * @Date: 2020/2/21
 */
@Slf4j
abstract class AbstractGetVarValue extends AbstractBase {
    /**
     * 数据体解析处理
     *
     * @param in      输入流
     * @param varType 变量类型
     * @return MapKey：变量索引、MapValue：变量值
     * @throws IOException e
     */
    Map<Integer, Object> bodyLogic(InputStream in, byte varType) throws IOException {
        // 取得变量类型的大分类
        String varCategory = AnalysisUtils.getVarTypeCategory(varType);

        Map<Integer, Object> result = new HashMap<>(100);
        for (;;) {
            // 获取数据包长度
            byte[] lenBuf = new byte[2];
            int bytes = SocketUtils.read(in, lenBuf);
            if (bytes != lenBuf.length) {
                log.error("The returned byte length is incorrect;");
                break;
            }
            int len = ByteUtils.bytesToInt(lenBuf);

            // 无数据或超过长度限制
            if (len <= 0 || len > Constants.DATA_PACKAGE_MAX) {
                break;
            }

            // 获取数据包的数据体
            byte[] bodyBuf = new byte[len];
            bytes = SocketUtils.read(in, bodyBuf);
            if (bytes != bodyBuf.length) {
                log.error("The returned byte length is incorrect;");
                break;
            }

            // 将数据体解析为对象
            int position = 0;
            while (position < len) {
                DViewVarInfo bean = new DViewVarInfo();
                position += AnalysisUtils.analysisValue(varCategory, bodyBuf, position, bean);
                result.put(bean.getIndex(), bean.getValue());
            }
        }

        log.debug("message:" + result.size());
        return result;
    }
}
