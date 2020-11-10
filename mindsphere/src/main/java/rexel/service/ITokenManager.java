package rexel.service;

import java.net.URISyntaxException;

/**
 * @Author: quchunhui
 * @Date: 2020/1/4
 * @Description:
 */
public interface ITokenManager extends IBaseService {
    String getToken(boolean bRefresh) throws URISyntaxException;
}
