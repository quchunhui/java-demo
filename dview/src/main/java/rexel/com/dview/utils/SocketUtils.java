package rexel.com.dview.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName: SocketUtils
 * @Description: Socket工具类
 * @Author: chunhui.qu@rexel.com.cn
 * @Date: 2020/2/21
 */
public class SocketUtils {
    public static int read(InputStream in, byte[] buffer) throws IOException {
        int iTimeOut = 5000;
        int millis = 50;
        int iWantRecNum = buffer.length;
        for (int i = 0; i < iTimeOut; i = i + millis) {
            int memLen = in.available();
            if (memLen >= iWantRecNum) {
                break;
            }
            try {
                Thread.sleep(millis);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        return in.read(buffer, 0, iWantRecNum);
    }
}
