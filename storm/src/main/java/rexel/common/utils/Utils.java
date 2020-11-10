package rexel.common.utils;

public class Utils {
    public static String StringFormat(int lenght, int _int) {
        // 0 代表前面补充0
        // 6 代表长度为6
        // d 代表参数为正数型
        return String.format("%0" + lenght + "d", _int);
    }
}
