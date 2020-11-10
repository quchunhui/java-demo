package rexel.service;

import rexel.pojo.ResultBean;

public interface IDeviceManager extends IBaseService {
    ResultBean getDeviceTypes(String token) throws Exception;
    ResultBean getDevices(String token) throws Exception;
}