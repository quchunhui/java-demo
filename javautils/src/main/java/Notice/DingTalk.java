package Notice;

import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * 发送钉钉机器人demo
 */
public class DingTalk {
    public static void main(String[] args) {
        // 获取安全密钥
        Long timestamp = System.currentTimeMillis();
        String sign = getSign(timestamp);

        // 钉钉的webhook
        String dingDingToken = "https://oapi.dingtalk.com/robot/send?access_token=0b34f860e45e5ab6f0b5977f2a7b208ccd3b23997bde94aa695b61e5c6eb6476"
            + "&timestamp=" + timestamp +"&sign=" + sign;

        JSONObject headJson = new JSONObject();
        headJson.put("Content-type", "application/json;charset=UTF-8");

        // 请求的JSON数据
        JSONObject textJson = new JSONObject();
        textJson.put("content", "小哥，你好。");
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("msgtype", "text");
        bodyJson.put("text", textJson);

        // 发送post请求
        String response = sendPost(dingDingToken, headJson, bodyJson);
        System.out.println("响应结果：" + response);
    }

    private static String getSign(Long timestamp) {
        String secret = "SECa2d1fd829dfa60a43467e44c243f4538fc46b44f4ce984451abb95439d3aedfc";

        String stringToSign = timestamp + "\n" + secret;
        String sign = "";
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sign;
    }

    private static String sendPost(String url, JSONObject headJson, JSONObject bodyJson) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            // 打开和URL之间的连接
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();

            // 设置请求头
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Fiddler");
            headJson.forEach((key, value) -> conn.setRequestProperty(key, String.valueOf(value)));

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(bodyJson);
            // flush输出流的缓冲
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return result.toString();
    }
}
