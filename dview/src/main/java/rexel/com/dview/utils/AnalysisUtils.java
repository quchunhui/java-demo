package rexel.com.dview.utils;

import rexel.com.dview.cons.Constants;
import java.nio.charset.Charset;
import rexel.com.dview.pojo.DViewVarInfo;

/**
 * @ClassName: AnalysisUtils
 * @Description: DView大数据接口数据解析共通类
 * @Author: chunhui.qu@rexel.com.cn
 * @Date: 2020/2/21
 */
public class AnalysisUtils {

    /**
     * 取得变量类型的大分类
     *
     * @param type 变量类型（16进制）
     * @return 变量类型大分类
     */
    public static String getVarTypeCategory(byte type) {
        if (type == 0x01 || type == 0x02 || type == 0x03 || type == 0x07) {
            // 变量类型:模拟(R8): AI=01,AO=02,AR=03,VA=07
            return Constants.VAR_CATEGORY_R8;
        } else if (type == 0x11 || type == 0x12 || type == 0x13 || type == 0x17) {
            // 变量类型:模拟(R4): AI=11,AO=12,AR=13,VA=17
            return Constants.VAR_CATEGORY_R4;
        } else if (type == 0x04 || type == 0x05 || type == 0x06 || type == 0x08) {
            // 变量类型:开关(I1): DI=04,DO=05,DR=06,VD=08
            return Constants.VAR_CATEGORY_I1;
        } else if (type == 0x09) {
            // 变量类型:文本(ST): VT=09
            return Constants.VAR_CATEGORY_ST;
        }

        return null;
    }

    /**
     * 根据变量类型大分类，解析接口返回的16进制数据体
     *
     * @param category [in]变量类型大分类
     * @param inBuf    [in]数据体
     * @param startPos [in]解析开始位置
     * @param outBean  [out]解析结果对象
     * @return 本次解析长度
     */
    public static int analysisValue(
            String category, byte[] inBuf, int startPos, DViewVarInfo outBean) {
        // R8: 解析为Double类型
        // R4: 解析为Float类型
        // I1: 解析为Int类型
        // ST: 解析为String类型
        int analysisLen;
        if (Constants.VAR_CATEGORY_R8.equals(category)) {
            analysisLen = analysisCategoryR8(inBuf, startPos, outBean);
        } else if (Constants.VAR_CATEGORY_R4.equals(category)) {
            analysisLen = analysisCategoryR4(inBuf, startPos, outBean);
        } else if (Constants.VAR_CATEGORY_I1.equals(category)) {
            analysisLen = analysisCategoryI1(inBuf, startPos, outBean);
        } else if (Constants.VAR_CATEGORY_ST.equals(category)) {
            analysisLen = analysisCategorySt(inBuf, startPos, outBean);
        } else {
            return -1;
        }

        return analysisLen;
    }

    /**
     * 解析16进制索引数据
     *
     * @param inBuf    [in]数据体
     * @param startPos [in]解析开始位置
     * @param outBean  [out]解析结果对象
     * @return 本次解析长度
     */
    public static int analysisIndex(byte[] inBuf, int startPos, DViewVarInfo outBean) {
        // 解析开始位置
        int position = startPos;

        // 获取单条数据索引
        byte[] indexBuf = new byte[Constants.INDEX_BYTE_LEN];
        System.arraycopy(inBuf, position, indexBuf, 0, indexBuf.length);
        int index = ByteUtils.bytesToInt(indexBuf);
        position += indexBuf.length;

        // 设置Bean
        outBean.setIndex(index);
        outBean.setName(null);
        outBean.setValue(null);

        // 返回解析长度
        return position - startPos;
    }

    /**
     * 解析16进制数据体，解析为Double类型
     *
     * @param inBuf    [in]数据体
     * @param startPos [in]解析开始位置
     * @param outBean  [out]解析结果对象
     * @return 本次解析长度
     */
    private static int analysisCategoryR8(byte[] inBuf, int startPos, DViewVarInfo outBean) {
        // 解析开始位置
        int position = startPos;

        // 获取单条数据索引
        byte[] indexBuf = new byte[Constants.INDEX_BYTE_LEN];
        System.arraycopy(inBuf, position, indexBuf, 0, indexBuf.length);
        int index = ByteUtils.bytesToInt(indexBuf);
        position += indexBuf.length;

        // 获取中间位
        byte[] formatBuf = new byte[1];
        System.arraycopy(inBuf, position, formatBuf, 0, formatBuf.length);
        position += formatBuf.length;

        // 获取单条数据值
        byte[] valueBuf = new byte[8];
        System.arraycopy(inBuf, position, valueBuf, 0, valueBuf.length);
        Object bodyStr = ByteUtils.bytesToDouble(valueBuf);
        position += valueBuf.length;

        // 设置Value
        // 设置Bean
        outBean.setIndex(index);
        outBean.setName(null);
        outBean.setValue(bodyStr);

        // 返回解析长度
        return position - startPos;
    }

    /**
     * 解析16进制数据体，解析为Float类型
     *
     * @param inBuf    [in]数据体
     * @param startPos [in]解析开始位置
     * @param outBean  [out]解析结果对象
     * @return 本次解析长度
     */
    private static int analysisCategoryR4(byte[] inBuf, int startPos, DViewVarInfo outBean) {
        int position = startPos;

        // 获取单条数据索引
        byte[] indexBuf = new byte[Constants.INDEX_BYTE_LEN];
        System.arraycopy(inBuf, position, indexBuf, 0, indexBuf.length);
        int index = ByteUtils.bytesToInt(indexBuf);
        position += indexBuf.length;

        // 获取中间位
        byte[] formatBuf = new byte[1];
        System.arraycopy(inBuf, position, formatBuf, 0, formatBuf.length);
        position += formatBuf.length;

        // 获取单条数据值
        byte[] valueBuf = new byte[4];
        System.arraycopy(inBuf, position, valueBuf, 0, valueBuf.length);
        Object bodyStr = ByteUtils.bytesToFloat(valueBuf);
        position += valueBuf.length;

        // 设置Bean
        outBean.setIndex(index);
        outBean.setName(null);
        outBean.setValue(bodyStr);

        // 返回解析长度
        return position - startPos;
    }

    /**
     * 解析16进制数据体，解析为Int类型
     *
     * @param inBuf    [in]数据体
     * @param startPos [in]解析开始位置
     * @param outBean  [out]解析结果对象
     * @return 本次解析长度
     */
    private static int analysisCategoryI1(byte[] inBuf, int startPos, DViewVarInfo outBean) {
        int position = startPos;

        // 获取单条数据索引
        byte[] indexBuf = new byte[Constants.INDEX_BYTE_LEN];
        System.arraycopy(inBuf, position, indexBuf, 0, indexBuf.length);
        int index = ByteUtils.bytesToInt(indexBuf);
        position += indexBuf.length;

        // 获取中间位
        byte[] formatBuf = new byte[1];
        System.arraycopy(inBuf, position, formatBuf, 0, formatBuf.length);
        position += formatBuf.length;

        // 设置Bean
        outBean.setIndex(index);
        outBean.setName(null);
        outBean.setValue(formatBuf[0]);

        // 返回解析长度
        return position - startPos;
    }

    /**
     * 解析16进制数据体，解析为String类型
     *
     * @param inBuf    [in]数据体
     * @param startPos [in]解析开始位置
     * @param outBean  [out]解析结果对象
     * @return 本次解析长度
     */
    private static int analysisCategorySt(byte[] inBuf, int startPos, DViewVarInfo outBean) {
        int position = startPos;

        // 获取单条数据索引
        byte[] indexBuf = new byte[Constants.INDEX_BYTE_LEN];
        System.arraycopy(inBuf, position, indexBuf, 0, indexBuf.length);
        int index = ByteUtils.bytesToInt(indexBuf);
        position += indexBuf.length;

        // 获取数据长度
        byte[] lenBuf = new byte[1];
        System.arraycopy(inBuf, position, lenBuf, 0, lenBuf.length);
        int len = ByteUtils.bytesToInt(lenBuf);
        position += lenBuf.length;

        // 获取数据值
        byte[] valueBuf = new byte[len];
        System.arraycopy(inBuf, position, valueBuf, 0, valueBuf.length);
        String bodyStr = ByteUtils.bytesToStr(
                valueBuf, Charset.forName(Constants.DVIEW_CHARSET_GBK));
        position += valueBuf.length;

        // 设置Bean
        outBean.setIndex(index);
        outBean.setName(null);
        outBean.setValue(bodyStr);

        // 返回解析长度
        return position - startPos;
    }
}