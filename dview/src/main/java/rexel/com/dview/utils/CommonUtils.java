package rexel.com.dview.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: CommonUtils
 * @Description: 共通函数类
 * @Author: chunhui.qu@rexel.com.cn
 * @Date: 2020/2/21
 */
public class CommonUtils {

    /**
     * 按照指定长度对List进行切分
     *
     * @param valueList 切分目标List
     * @return 切分结果
     */
    public static List<List<Object>> listSplit(List<Object> valueList, int splitLen) {
        List<List<Object>> result = new ArrayList<>();

        for (int fromIndex = 0; fromIndex < valueList.size(); ) {
            int remainCount = valueList.size() - fromIndex;
            if (remainCount >= splitLen) {
                result.add(valueList.subList(fromIndex, fromIndex + splitLen));
            } else {
                result.add(valueList.subList(fromIndex, fromIndex + remainCount));
            }
            fromIndex += splitLen;
        }

        return result;
    }

    /**
     * 获取系统时间
     *
     * @return 系统时间
     */
    public static String getFullSysDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(cal.getTime());
    }
}