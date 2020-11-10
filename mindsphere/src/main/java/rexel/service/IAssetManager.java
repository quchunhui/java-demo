package rexel.service;

import rexel.pojo.ResultBean;

public interface IAssetManager extends IBaseService {
    ResultBean getAssetTypes(String token, String size, String page, String sort) throws Exception;
    ResultBean getAssets(String token, String size, String page, String sort) throws Exception;
}
