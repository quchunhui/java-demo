package rexel.com.dview.utils;

import com.google.common.primitives.Bytes;
import rexel.com.dview.cons.Constants;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import rexel.com.dview.pojo.DViewVarInfo;

/**
 * @ClassName: ParamUtils
 * @Description: DView参数共通函数类
 * @Author: chunhui.qu@rexel.com.cn
 * @Date: 2020/2/21
 */
public class ParamUtils {
    /**
     * [1].获取某类型变量名称及变量索引
     *
     * @param varType    变量类型（16进制）
     * @param startIndex 开始索引（0-800000）
     * @param varCount   变量数量（0:全部,1-800000个）
     * @return 16进制参数
     */
    public static byte[] makeParam1(byte varType, int startIndex, int varCount) {
        byte[] indexArrays = ByteUtils.intToBytes(startIndex, Constants.INDEX_BYTE_LEN);
        byte[] countArrays = ByteUtils.intToBytes(varCount, Constants.COUNT_BYTE_LEN);

        List<Byte> list = new ArrayList<>();
        // Buffer[0][1] 3E 2A 请求标识
        list.add((byte) 0x3E);
        list.add((byte) 0x2A);

        // Buffer[2][3] 27 1B 功能码(10011)
        list.add((byte) 0x27);
        list.add((byte) 0x1B);

        // Buffer[4] 变量类型
        // 获取变量名: AI=01,AO=02,AR=03,VA=07 DI=04,DO=05,DR=06,VD=08 VT=09
        // 获取变量名.附量程: AI=11,AO=12,AR=13,VA=17
        // 获取变量名.附量程及报警: AI=21,AO=22,AR=23
        list.add(varType);

        // Buffer[5][6][7] 开始索引 0-800000 高字节前,低字节后
        for (byte b : indexArrays) {
            list.add(b);
        }

        // Buffer[8][9][10] 变量数量 0:最大数量,1-800000:部分 高字节前,低字节后
        for (byte b : countArrays) {
            list.add(b);
        }

        return Bytes.toArray(list);
    }

    /**
     * [2].通过变量索引读取变量值,或某段时间内发生变化的变量值
     *
     * @param varType    变量类型（16进制）
     * @param startIndex 开始索引（0-800000）
     * @param varCount   变量数量（0:全部,1-800000个）
     * @param timeRange  秒时间段（0:全部,1-3600秒）
     * @return 16进制参数
     */
    public static byte[] makeParam2(byte varType, int startIndex, int varCount, int timeRange) {
        byte[] indexArrays = ByteUtils.intToBytes(startIndex, Constants.INDEX_BYTE_LEN);
        byte[] countArrays = ByteUtils.intToBytes(varCount, Constants.COUNT_BYTE_LEN);
        byte[] rangeArrays = ByteUtils.intToBytes(timeRange, Constants.RANGE_BYTE_LEN);

        List<Byte> list = new ArrayList<>();
        // Buffer[0][1] 3E 2A 请求标识
        list.add((byte) 0x3E);
        list.add((byte) 0x2A);

        // Buffer[2][3] 27 1C 功能码(10012)
        list.add((byte) 0x27);
        list.add((byte) 0x1C);

        // Buffer[4] 变量类型
        // 模拟(R8): AI=01,AO=02,AR=03,VA=07
        // 开关(I1): DI=04,DO=05,DR=06,VD=08
        // 文本(ST): VT=09
        // 0x10+XX 模拟(R4): AI=11,AO=12,AR=13,VA=17
        // 0x20+XX 读取处于报警状态的变量
        // 0x40+XX 附加变量时间戳:4字节.距2009-8-1零点秒数
        list.add(varType);

        // Buffer[5][6][7] 开始索引 0-800000 高字节前,低字节后
        for (byte b : indexArrays) {
            list.add(b);
        }

        // Buffer[8][9][10] 变量数量 0:最大数量,1-800000:部分 高字节前,低字节后
        for (byte b : countArrays) {
            list.add(b);
        }

        // Buffer[11][12] 秒时间段 0:全部,1-3600秒 高字节前,低字节后
        for (byte b : rangeArrays) {
            list.add(b);
        }

        return Bytes.toArray(list);
    }

    /**
     * [3].通过变量索引,选择读取某些变量值
     *
     * @param varType   变量类型（16进制）
     * @param timeRange 秒时间段（0:全部,1-3600秒）
     * @param indexList 变量索引列表
     * @return 16进制参数
     */
    public static byte[] makeParam3(byte varType, int timeRange, List<Object> indexList) {
        // 计算索引总字节数
        List<Byte> indexByteList = new ArrayList<>();
        for (Object obj : indexList) {
            Integer index = (Integer) obj;
            byte[] indexArrays = ByteUtils.intToBytes(index, Constants.INDEX_BYTE_LEN);
            for (byte b : indexArrays) {
                indexByteList.add(b);
            }
        }

        // 秒时间段转字节数组
        byte[] rangeArrays = ByteUtils.intToBytes(timeRange, Constants.RANGE_BYTE_LEN);
        // 索引字节数转字节数组
        byte[] indexBytesSum = ByteUtils.intToBytes(indexByteList.size(), 2);

        List<Byte> list = new ArrayList<>();
        // Buffer[0][1] 3E 2A 请求标识
        list.add((byte) 0x3E);
        list.add((byte) 0x2A);

        // Buffer[2][3] 27 1E 功能码(10014)
        list.add((byte) 0x27);
        list.add((byte) 0x1E);

        // Buffer[4] 变量类型
        // 模拟(R8): AI=01,AO=02,AR=03,VA=07
        // 开关(I1): DI=04,DO=05,DR=06,VD=08
        // 文本(ST): VT=09
        // 0x10+XX 模拟(R4): AI=11,AO=12,AR=13,VA=17
        // 0x20+XX 读取处于报警状态的变量
        // 0x40+XX 附加变量时间戳:4字节.距2009-8-1零点秒数
        list.add(varType);

        // Buffer[5][6] 秒时间段 0:全部,1-3600秒 高字节前,低字节后
        for (byte b : rangeArrays) {
            list.add(b);
        }

        // Buffer[7][8] 索引字节数 0-8192 高字节前,低字节后
        for (byte b : indexBytesSum) {
            list.add(b);
        }
        // 多个索引编号 最多2730个变量索引编号 每变量索引编号,3字节,高字节前,低字节后
        list.addAll(indexByteList);

        return Bytes.toArray(list);
    }

    /**
     * [4].通过变量索引,批量修改变量值
     *
     * @param varType  变量类型（16进制）
     * @param beanList DViewVarInfo列表
     * @return 16进制参数
     */
    public static byte[] makeParam4(byte varType, List<Object> beanList) {
        String varCategory = AnalysisUtils.getVarTypeCategory(varType);

        // 根据变量类型大分类，解析为不同的16进制参数
        List<Byte> beanBytesList = null;
        if (Constants.VAR_CATEGORY_R8.equals(varCategory)) {
            beanBytesList = makeParamCategoryR8(beanList);
        } else if (Constants.VAR_CATEGORY_R4.equals(varCategory)) {
            beanBytesList = makeParamCategoryR4(beanList);
        } else if (Constants.VAR_CATEGORY_I1.equals(varCategory)) {
            beanBytesList = makeParamCategoryI1(beanList);
        } else if (Constants.VAR_CATEGORY_ST.equals(varCategory)) {
            beanBytesList = makeParamCategorySt(beanList);
        }
        if (beanBytesList == null || beanBytesList.size() <= 0) {
            return null;
        }

        // 数据包长度转字节数组
        byte[] packageBytes = ByteUtils.intToBytes(beanBytesList.size(), 2);

        List<Byte> list = new ArrayList<>();
        // Buffer[0][1] 3E 2A 请求标识
        list.add((byte) 0x3E);
        list.add((byte) 0x2A);

        // Buffer[2][3] 27 1D 功能码(10013)
        list.add((byte) 0x27);
        list.add((byte) 0x1D);

        // Buffer[4] 变量类型
        // 模拟(R8): AI=01,AO=02,AR=03,VA=07
        // 开关(I1): DI=04,DO=05,DR=06,VD=08
        // 文本(ST): VT=09
        list.add(varType);

        // Buffer[5][6] 数据包长度 0-8192 高字节前,低字节后
        for (byte b : packageBytes) {
            list.add(b);
        }

        // 数据包 格式：[index] : 变量索引编号,3字节,高字节前,低字节后
        list.addAll(beanBytesList);

        return Bytes.toArray(list);
    }

    /**
     * [5].获取某类型变量名称及变量索引
     *
     * @param varType  变量类型（16进制）
     * @param nameList 变量名称列表
     * @return 16进制参数
     */
    public static byte[] makeParam5(byte varType, List<String> nameList) {
        // 将变量名称解析为16进制byte数组
        List<Byte> nameListNew = makeVarNames(nameList);
        if (nameListNew == null) {
            return null;
        }

        // 数据包长度转字节数组
        byte[] packageBytes = ByteUtils.intToBytes(nameListNew.size(), 2);

        List<Byte> list = new ArrayList<>();
        // Buffer[0][1] 3E 2A 请求标识
        list.add((byte) 0x3E);
        list.add((byte) 0x2A);

        // Buffer[2][3] 27 1F 功能码(10015)
        list.add((byte) 0x27);
        list.add((byte) 0x1F);

        // Buffer[4] 变量类型
        // AI=1,AO=2,AR=3,VA=7,DI=4,DO=5,DR=6,VD=8,VT=9
        // 0=混合变量,变量名包含类型,格式:'XX.YYYYYY'
        list.add(varType);

        // Buffer[5][6] 数据包长度 0-8192 高字节前,低字节后
        for (byte b : packageBytes) {
            list.add(b);
        }

        // 变量名列表（多变量名,'|'分隔）
        list.addAll(nameListNew);

        return Bytes.toArray(list);
    }

    /**
     * 变量名列表由String转为byte
     *
     * @param nameList 变量名列表
     * @return byte类型变量名列表
     */
    private static List<Byte> makeVarNames(List<String> nameList) {
        List<Byte> result = new ArrayList<>();

        // '|'字符的byte数组
        byte[] markBytes = ByteUtils.strToBytes(
                Constants.VAR_NAME_DIVIDE_MARK, Charset.forName(Constants.DVIEW_CHARSET_GBK));

        // 设置'|'开头
        for (byte b : markBytes) {
            result.add(b);
        }

        // 转换为byte
        for (String name : nameList) {
            byte[] nameBytes = ByteUtils.strToBytes(
                    name, Charset.forName(Constants.DVIEW_CHARSET_GBK));
            for (byte b1 : nameBytes) {
                result.add(b1);
            }
            for (byte b2 : markBytes) {
                result.add(b2);
            }
        }

        return result;
    }

    /**
     * 根据变量类型大分类，解析为不同的16进制参数（解析为Double类型）
     *
     * @param beanList DViewVarInfo列表
     * @return byte列表
     */
    private static List<Byte> makeParamCategoryR8(List<Object> beanList) {
        List<Byte> result = new ArrayList<>();

        for (Object obj : beanList) {
            DViewVarInfo bean = (DViewVarInfo) obj;
            byte[] indexLen = ByteUtils.intToBytes(bean.getIndex(), Constants.INDEX_BYTE_LEN);
            for (byte b : indexLen) {
                result.add(b);
            }

            double value = Double.valueOf(String.valueOf(bean.getValue()));
            byte[] valueBytes = ByteUtils.doubleToBytes(value);

            for (byte b : valueBytes) {
                result.add(b);
            }
        }

        return result;
    }

    /**
     * 根据变量类型大分类，解析为不同的16进制参数（解析为Float类型）
     *
     * @param beanList DViewVarInfo列表
     * @return byte列表
     */
    private static List<Byte> makeParamCategoryR4(List<Object> beanList) {
        List<Byte> result = new ArrayList<>();

        for (Object obj : beanList) {
            DViewVarInfo bean = (DViewVarInfo) obj;
            byte[] indexLen = ByteUtils.intToBytes(bean.getIndex(), Constants.INDEX_BYTE_LEN);
            for (byte b : indexLen) {
                result.add(b);
            }

            float value = Float.valueOf(String.valueOf(bean.getValue()));
            byte[] valueBytes = ByteUtils.floatToBytes(value);

            for (byte b : valueBytes) {
                result.add(b);
            }
        }

        return result;
    }

    /**
     * 根据变量类型大分类，解析为不同的16进制参数（解析为Int类型）
     *
     * @param beanList DViewVarInfo列表
     * @return byte列表
     */
    private static List<Byte> makeParamCategoryI1(List<Object> beanList) {
        List<Byte> result = new ArrayList<>();

        for (Object obj : beanList) {
            DViewVarInfo bean = (DViewVarInfo) obj;
            byte[] indexLen = ByteUtils.intToBytes(bean.getIndex(), Constants.INDEX_BYTE_LEN);
            for (byte b : indexLen) {
                result.add(b);
            }

            int value = Integer.valueOf(String.valueOf(bean.getValue()));
            byte[] valueBytes = ByteUtils.intToBytes(value, 1);

            for (byte b : valueBytes) {
                result.add(b);
            }
        }

        return result;
    }

    /**
     * 根据变量类型大分类，解析为不同的16进制参数（解析为String类型）
     *
     * @param beanList DViewVarInfo列表
     * @return byte列表
     */
    private static List<Byte> makeParamCategorySt(List<Object> beanList) {
        List<Byte> result = new ArrayList<>();

        for (Object obj : beanList) {
            DViewVarInfo bean = (DViewVarInfo) obj;
            byte[] indexLen = ByteUtils.intToBytes(bean.getIndex(), Constants.INDEX_BYTE_LEN);
            for (byte b : indexLen) {
                result.add(b);
            }

            String value = String.valueOf(bean.getValue());
            byte[] valueBytes = ByteUtils.strToBytes(
                    value, Charset.forName(Constants.DVIEW_CHARSET_GBK));

            for (byte b : valueBytes) {
                result.add(b);
            }
        }

        return result;
    }
}