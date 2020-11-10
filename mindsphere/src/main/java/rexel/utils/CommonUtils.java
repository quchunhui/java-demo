package rexel.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class CommonUtils {
    public static HttpHeaders initHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return headers;
    }


}
