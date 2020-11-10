package com.rexel.tdengine.utils;

import com.rexel.tdengine.pojo.PointInfo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName CommonUtils
 * @Description CommonUtils
 * @Author: chunhui.qu
 * @Date: 2020/10/16
 */
public class CommonUtils {
    public static String timeLongToStr(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(new Date(time));
    }

    public static List<List<PointInfo>> listSplit(List<PointInfo> valueList, int splitLen) {
        List<List<PointInfo>> result = new ArrayList<>();

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
}
