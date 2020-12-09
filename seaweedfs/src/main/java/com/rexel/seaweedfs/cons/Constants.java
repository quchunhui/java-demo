package com.rexel.seaweedfs.cons;

/**
 * @ClassName Constants
 * @Description Constants
 * @Author: chunhui.qu
 * @Date: 2020/12/9
 */
public class Constants {
    public static final String CURL_PARAM_SOUR = "{source}";
    public static final String CURL_PARAM_DESC = "{destination}";
    public static final String CURL_UPLOAD = "curl -F file=@{source} {destination}";
    public static final String CURL_DELETE = "curl -X DELETE {destination}?recursive=true&ignoreRecursiveError=true";
}
