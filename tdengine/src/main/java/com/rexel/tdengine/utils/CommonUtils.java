package com.rexel.tdengine.utils;

import com.rexel.tdengine.pojo.PointInfo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName CommonUtils
 * @Description CommonUtils
 * @Author: chunhui.qu
 * @Date: 2020/10/16
 */
public class CommonUtils {
    private final static String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public static String timeLongToStr(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        return sdf.format(new Date(time));
    }

    public static Date timeStrToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Date getNextSecond(Date date, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, interval);
        return calendar.getTime();
    }

    public static Date getNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
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
