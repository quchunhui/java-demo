package com.rexel.seaweedfs.api;

import com.rexel.seaweedfs.cons.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @ClassName FileUpload
 * @Description FileUpload
 * @Author: chunhui.qu
 * @Date: 2020/12/9
 */
public class FileUpload {
    public static void main(String[] args) {
        // curl -F file=@/home/radmin/soft/nice.jpg 192.168.29.100:10001/3,01c226517c
        String fileName = "预览图_千图网_编号35901643.png";
        String fromPath = "C:\\Users\\Administrator\\Pictures\\qiantu\\" + fileName;
        String toPath = "http://rexel-ids001:8888/ids/" + fileName;
        String[] commands = formatUploadUrl(fromPath, toPath);

        System.out.println(execCurl(commands));
    }

    private static String[] formatUploadUrl(String from, String to) {
        String url = Constants.CURL_UPLOAD;
        url = url.replace(Constants.CURL_PARAM_SOUR, from);
        url = url.replace(Constants.CURL_PARAM_DESC, to);
        return url.split(" ");
    }

    private static String execCurl(String[] commands) {
        ProcessBuilder process = new ProcessBuilder(commands);
        Process p;
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            return builder.toString();
        } catch (IOException e) {
            System.out.print("error");
            e.printStackTrace();
        }
        return null;

    }
}
