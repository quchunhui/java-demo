package rexel.com.dview.utils;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * @ClassName: ByteUtils
 * @Description: 字节处理共通类
 * @Author: chunhui.qu@rexel.com.cn
 * @Date: 2020/2/21
 */
public class ByteUtils implements Serializable {
    /**
     * byte[]转换为String
     *
     * @param arr 16进制byte数组
     * @return 字符串
     */
    public static String bytesToStr(byte[] arr, Charset charset) {
        return new String(arr, charset);
    }

    /**
     * String转换为byte[]
     *
     * @param str 字符串
     * @return 16进制byte数组
     */
    public static byte[] strToBytes(String str, Charset charset) {
        return str.getBytes(charset);
    }

    /**
     * byte[]转换为int
     *
     * @param arr 16进制byte数组
     * @return int数值
     */
    public static int bytesToInt(byte[] arr) {
        int a = 0;
        for (int i = 0; i < arr.length; i++) {
            a += (arr[i] & 0x000000ff) << 8 * (arr.length - 1 - i);
        }
        return a;
    }

    /**
     * int转换为byte[]
     *
     * @param a      int数值
     * @param length 转换长度
     * @return 16进制byte数组
     */
    public static byte[] intToBytes(int a, int length) {
        byte[] arr = new byte[length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (byte) (a >> 8 * (arr.length - i - 1));
        }
        return arr;
    }

    /**
     * byte[]转换为float
     *
     * @param arr 16进制byte数组
     * @return float数值
     */
    public static float bytesToFloat(byte[] arr) {
        int value = bytesToInt(arr);
        return Float.intBitsToFloat(value);
    }

    /**
     * float转换为byte[]
     *
     * @param f float数值
     * @return 16进制byte数组
     */
    public static byte[] floatToBytes(float f) {
        int value = Float.floatToRawIntBits(f);
        return intToBytes(value, 4);
    }

    /**
     * byte[]转换为double
     *
     * @param arr 16进制byte数组
     * @return doubule数值
     */
    public static double bytesToDouble(byte[] arr) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (arr[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(value);
    }

    /**
     * doublt转换为byte[]
     *
     * @param d double数值
     * @return 16进制byte数组
     */
    public static byte[] doubleToBytes(double d) {
        long value = Double.doubleToRawLongBits(d);
        byte[] byteRet = new byte[8];
        for (int i = 0; i < 8; i++) {
            byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
        }
        return byteRet;
    }
}
