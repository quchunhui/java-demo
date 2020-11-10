package rexel.common;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rexel.config.PropertiesConfig;

@Component
@Slf4j
public class Utils {
    private PropertiesConfig propertiesConfig;
    private String deviceUnique = null;

    @Autowired
    public void setPropertiesConfig(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }

    public String makeUriString (String api) {
        return propertiesConfig.getAddress() + api;
    }

    public String dateToStr(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(cal.getTime());
    }

    public String toSampleDate(String str) {
        Date date = strToDate(str);
        if (date == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(cal.getTime());
    }

    public String minusMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(cal.getTime());
    }

    public String getDeviceUnique () {
        if (deviceUnique != null) {
            return deviceUnique;
        }

        String property;
        String serial;
        try {
            Process process = Runtime.getRuntime().exec(
                new String[] { "wmic", "cpu", "get", "ProcessorId" });
            process.getOutputStream().close();

            Scanner sc = new Scanner(process.getInputStream());
            property = sc.next();
            serial = sc.next();
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        log.debug(property + ":" + serial);
        deviceUnique = serial;

        return serial;
    }

    public JSONObject responseJson(int code, String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        return jsonObject;
    }

    private Date strToDate(String str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(str);
        } catch (Exception ignored) {
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            return sdf.parse(str);
        } catch (Exception ignored) {
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.parse(str);
        } catch (Exception ignored) {
        }

        return null;
    }
}